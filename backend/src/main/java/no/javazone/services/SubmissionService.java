package no.javazone.services;

import no.javazone.integrations.sleepingpill.SleepingPillClient;
import no.javazone.integrations.sleepingpill.model.Conferences;
import no.javazone.integrations.sleepingpill.model.Session;
import no.javazone.integrations.sleepingpill.model.Sessions;
import no.javazone.representations.Submission;
import no.javazone.representations.SubmissionsForUser;
import no.javazone.representations.Year;
import no.javazone.session.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class SubmissionService {

    // TODO (EHH): This concept needs to be nuked, probably when we integrate with sleepingpill
    public static final String CURRENT_YEAR = "2017";

    private final SleepingPillClient sleepingPill;

    // TODO (EHH): Integrate with sleepingpill instead of in memory store...
    private Map<AuthenticatedUser, SubmissionsForUser> submissions = new HashMap<>();

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
                e.getValue().stream().map(s -> new Submission(s.sessionId, "title")).collect(toList())
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

    public void submitNewTalk(AuthenticatedUser authenticatedUser, Submission submission) {
        submitNewTalkForYear(authenticatedUser, submission, CURRENT_YEAR);
    }

    private void submitNewTalkForYear(AuthenticatedUser authenticatedUser, Submission submission, String year) {
        submissions.computeIfAbsent(authenticatedUser, (a) -> new SubmissionsForUser(new ArrayList<>())).addForYear(year, submission);
    }

    public void debugDataset(AuthenticatedUser authenticatedUser) {
        submitNewTalkForYear(authenticatedUser, new Submission("1", "Why Hibernate is Awezome!"), "2010");
        submitNewTalkForYear(authenticatedUser, new Submission("2", "I still think Hibernate is awezome, even though my colleagues tease me!"), "2011");
        submitNewTalkForYear(authenticatedUser, new Submission("3", "Well, now it's getting awkward, they really hate hibernate? WHY!?!"), "2012");
        submitNewTalkForYear(authenticatedUser, new Submission("4", "Actually, hibernate kinda sucks!"), "2014");
        submitNewTalkForYear(authenticatedUser, new Submission("5", "Databases? Pfft, that's sooo last year! Frontend is my new passion!"), "2016");
        submitNewTalkForYear(authenticatedUser, new Submission("6", "React is the new Hibernate!"), "2016");
        submitNewTalkForYear(authenticatedUser, new Submission("7", "Haaaaave you met Elm?!"), "2017");
    }
}
