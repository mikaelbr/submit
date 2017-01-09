package no.javazone.services;

import no.javazone.integrations.sleepingpill.SleepingPillClient;
import no.javazone.integrations.sleepingpill.model.common.SessionStatus;
import no.javazone.integrations.sleepingpill.model.create.CreatedSession;
import no.javazone.integrations.sleepingpill.model.create.NewSession;
import no.javazone.integrations.sleepingpill.model.get.Conferences;
import no.javazone.integrations.sleepingpill.model.get.Session;
import no.javazone.integrations.sleepingpill.model.get.Sessions;
import no.javazone.integrations.sleepingpill.model.update.UpdatedSession;
import no.javazone.representations.Submission;
import no.javazone.representations.SubmissionsForUser;
import no.javazone.representations.Year;
import no.javazone.session.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class SubmissionService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public static final String SUBMIT_YEAR = "javazone_2016";

    private final SleepingPillClient sleepingPill;

    private final Conferences conferences;

    @Autowired
    public SubmissionService(SleepingPillClient sleepingPill) {
        this.sleepingPill = sleepingPill;

        // TODO (EHH): Refresh this periodically?
        conferences = sleepingPill.getConferences();
    }

    public SubmissionsForUser getSubmissionsForUser(AuthenticatedUser authenticatedUser) {
        Sessions sleepingPillSessions = sleepingPill.getTalksForSpeakerByEmail(authenticatedUser.emailAddress.toString());

        Map<String, List<Session>> groupedByConference = sleepingPillSessions.sessions.stream().collect(groupingBy(s -> s.conferenceId));

        return new SubmissionsForUser(
                groupedByConference.entrySet().stream()
                        .map(this::sleepingpillYearToOurYear)
                        .sorted(comparing(y -> y.year))
                        .collect(toList())
        );
    }

    private Year sleepingpillYearToOurYear(Map.Entry<String, List<Session>> e) {
        return new Year(
                conferences.getNameFromId(e.getKey()),
                e.getValue().stream().map(Submission::fromSleepingPillSession).collect(toList())
        );
    }

    public Submission getSubmissionForUser(AuthenticatedUser authenticatedUser, String submissionId) {
        Session session = sleepingPill.getSession(submissionId);
        if(session.speakers.stream().anyMatch(s -> s.email.equals(authenticatedUser.emailAddress.toString()))) {
            return Submission.fromSleepingPillSession(session);
        } else {
            LOG.warn(format("User %s tried to access session %s, which the user is not a speaker for...", authenticatedUser.emailAddress.toString(), submissionId));
            throw new NotFoundException("Session with ID " + submissionId + " not found");
        }
    }

    public Submission createNewDraft(AuthenticatedUser authenticatedUser) {
        NewSession draft = NewSession.draft(authenticatedUser.emailAddress);
        String conferenceId = conferences.getIdFromSlug(SUBMIT_YEAR);
        CreatedSession createdSession = sleepingPill.createSession(conferenceId, draft);
        return Submission.fromSleepingPillCreatedSession(conferenceId, draft, createdSession);
    }

    public Submission updateSubmission(AuthenticatedUser authenticatedUser, String submissionId, Submission submission) {
        // TODO (EHH): add missing things: keywords, speakers etc...
        UpdatedSession updatedSession = new UpdatedSession(
                parseAndValidateStatus(authenticatedUser, submission),
                submission.title,
                submission.theAbstract,
                submission.intendedAudience,
                submission.format,
                submission.language,
                submission.outline
        );
        sleepingPill.updateSession(submissionId, updatedSession);

        return getSubmissionForUser(authenticatedUser, submissionId);
    }

    private SessionStatus parseAndValidateStatus(AuthenticatedUser authenticatedUser, Submission submission) {
        SessionStatus status = SessionStatus.valueOf(submission.status);
        if(status == SessionStatus.DRAFT || status == SessionStatus.SUBMITTED) {
            return status;
        } else {
            LOG.warn(String.format("%s tried to set the status %s that the user isn't allowed to set...", authenticatedUser.emailAddress.toString(), status.name()));
            throw new ForbiddenException("Tried to set a status that the user isn't allowed to set...");
        }
    }
}
