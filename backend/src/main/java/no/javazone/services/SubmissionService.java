package no.javazone.services;

import no.javazone.integrations.sleepingpill.SleepingPillClient;
import no.javazone.integrations.sleepingpill.model.create.CreatedSession;
import no.javazone.integrations.sleepingpill.model.create.NewSession;
import no.javazone.integrations.sleepingpill.model.get.Conferences;
import no.javazone.integrations.sleepingpill.model.get.Session;
import no.javazone.integrations.sleepingpill.model.get.Sessions;
import no.javazone.representations.Submission;
import no.javazone.representations.SubmissionsForUser;
import no.javazone.representations.Year;
import no.javazone.session.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class SubmissionService {

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
        // TODO (EHH): Yuck!
        SubmissionsForUser all = getSubmissionsForUser(authenticatedUser);
        for (Year year : all.years) {
            for (Submission s : year.submissions) {
                if (submissionId.equals(s.id)) {
                    return s;
                }
            }
        }
        return null;
    }

    public Submission createNewDraft(AuthenticatedUser authenticatedUser) {
        NewSession draft = NewSession.draft(authenticatedUser.emailAddress);
        String conferenceId = conferences.getIdFromSlug(SUBMIT_YEAR);
        CreatedSession createdSession = sleepingPill.createSession(conferenceId, draft);
        return Submission.fromSleepingPillCreatedSession(conferenceId, draft, createdSession);
    }

}
