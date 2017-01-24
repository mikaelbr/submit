package no.javazone.submit.services;

import no.javazone.submit.config.SleepingPillConfiguration;
import no.javazone.submit.integrations.sleepingpill.SleepingPillClient;
import no.javazone.submit.integrations.sleepingpill.model.common.SessionStatus;
import no.javazone.submit.integrations.sleepingpill.model.create.CreatedSession;
import no.javazone.submit.integrations.sleepingpill.model.create.NewSession;
import no.javazone.submit.integrations.sleepingpill.model.get.Conferences;
import no.javazone.submit.integrations.sleepingpill.model.get.Session;
import no.javazone.submit.integrations.sleepingpill.model.get.Sessions;
import no.javazone.submit.integrations.sleepingpill.model.update.UpdatedSession;
import no.javazone.submit.integrations.sleepingpill.model.update.UpdatedSpeaker;
import no.javazone.submit.api.representations.Speaker;
import no.javazone.submit.api.representations.Submission;
import no.javazone.submit.api.representations.SubmissionsForUser;
import no.javazone.submit.api.representations.Year;
import no.javazone.submit.api.session.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class SubmissionService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final SleepingPillClient sleepingPill;

    private final Conferences conferences;

    private final SleepingPillConfiguration sleepingPillConfiguration;

    @Autowired
    public SubmissionService(SleepingPillClient sleepingPill, SleepingPillConfiguration sleepingPillConfiguration) {
        this.sleepingPill = sleepingPill;

        // TODO (EHH): Refresh this periodically?
        conferences = sleepingPill.getConferences();
        this.sleepingPillConfiguration = sleepingPillConfiguration;
    }

    public SubmissionsForUser getSubmissionsForUser(AuthenticatedUser authenticatedUser) {
        Sessions sleepingPillSessions = sleepingPill.getTalksForSpeakerByEmail(authenticatedUser.emailAddress.toString());

        Map<String, List<Session>> groupedByConference = sleepingPillSessions.sessions.stream().collect(groupingBy(s -> s.conferenceId));

        return new SubmissionsForUser(
                groupedByConference.entrySet().stream()
                        .map(this::sleepingpillYearToOurYear)
                        .sorted(Comparator.<Year, String>comparing(y -> y.year).reversed())
                        .collect(toList())
        );
    }

    private Year sleepingpillYearToOurYear(Map.Entry<String, List<Session>> e) {
        return new Year(
                conferences.getNameFromId(e.getKey()),
                e.getValue().stream().map((session) -> fromSleepingPillSession(session)).collect(toList())
        );
    }

    public Submission getSubmissionForUser(AuthenticatedUser authenticatedUser, String submissionId) {
        Session session = sleepingPill.getSession(submissionId);
        if (session.speakers.stream().anyMatch(s -> s.email.equals(authenticatedUser.emailAddress.toString()))) {
            return fromSleepingPillSession(session);
        } else {
            LOG.warn(format("User %s tried to access session %s, which the user is not a speaker for...", authenticatedUser.emailAddress.toString(), submissionId));
            throw new NotFoundException("Session with ID " + submissionId + " not found");
        }
    }

    public Submission createNewDraft(AuthenticatedUser authenticatedUser) {
        NewSession draft = NewSession.draft(authenticatedUser.emailAddress);
        String conferenceId = conferences.getIdFromSlug(sleepingPillConfiguration.activeYear);
        CreatedSession createdSession = sleepingPill.createSession(conferenceId, draft);
        return getSubmissionForUser(authenticatedUser, createdSession.id);
    }

    public Submission updateSubmission(AuthenticatedUser authenticatedUser, String submissionId, Submission submission) {
        Submission previousSubmission = getSubmissionForUser(authenticatedUser, submissionId);

        if (!isEditableBySubmitter(previousSubmission.conferenceId)) {
            LOG.warn(String.format("User %s tried to edit a previous year's submissions with is %s", authenticatedUser.emailAddress, submissionId));
            throw new ForbiddenException("Not allowed to edit previous year's submissions");
        }

        UpdatedSession updatedSession = new UpdatedSession(
                parseAndValidateStatus(authenticatedUser, submission),
                submission.title,
                submission.theAbstract,
                submission.intendedAudience,
                submission.format,
                submission.language,
                submission.outline,
                submission.equipment,
                submission.length,
                submission.speakers.stream().map(UpdatedSpeaker::fromApiObject).collect(toList())
        );
        sleepingPill.updateSession(submissionId, updatedSession);

        return getSubmissionForUser(authenticatedUser, submissionId);
    }

    private SessionStatus parseAndValidateStatus(AuthenticatedUser authenticatedUser, Submission submission) {
        SessionStatus status = SessionStatus.valueOf(submission.status);
        if (status == SessionStatus.DRAFT || status == SessionStatus.SUBMITTED) {
            return status;
        } else {
            LOG.warn(String.format("%s tried to set the status %s that the user isn't allowed to set...", authenticatedUser.emailAddress.toString(), status.name()));
            throw new ForbiddenException("Tried to set a status that the user isn't allowed to set...");
        }
    }

    private Submission fromSleepingPillSession(Session session) {
        return new Submission(
                session.sessionId,
                session.conferenceId,
                session.status.name(),
                session.getTitle(),
                session.getAbstract(),
                session.getIntendedAudience(),
                session.getFormat(),
                session.getLanguage(),
                session.getOutline(),
                session.getEquipment(),
                session.getLength(),
                session.speakers.stream().map(Speaker::fromSleepingPillSpeaker).collect(toList()),
                isEditableBySubmitter(session.conferenceId)
        );
    }

    private boolean isEditableBySubmitter(String sessionId) {
        return conferences.getSlugFromId(sessionId).equals(sleepingPillConfiguration.activeYear);
    }
}
