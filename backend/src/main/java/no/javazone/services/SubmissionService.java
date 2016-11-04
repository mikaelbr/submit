package no.javazone.services;

import no.javazone.representations.Submission;
import no.javazone.representations.SubmissionsForUser;
import no.javazone.session.AuthenticatedUser;

import java.util.HashMap;
import java.util.Map;

public class SubmissionService {

    // TODO (EHH): This concept needs to be nuked, probably when we integrate with sleepingpill
    public static final String CURRENT_YEAR = "2017";

    // TODO (EHH): Integrate with sleepingpill instead of in memory store...
    private Map<AuthenticatedUser, SubmissionsForUser> submissions = new HashMap<>();

    public SubmissionsForUser getSubmissionsForUser(AuthenticatedUser authenticatedUser) {
        return submissions.getOrDefault(authenticatedUser, new SubmissionsForUser());
    }

    public void submitNewTalk(AuthenticatedUser authenticatedUser, Submission submission) {
        submitNewTalkForYear(authenticatedUser, submission, CURRENT_YEAR);
    }

    private void submitNewTalkForYear(AuthenticatedUser authenticatedUser, Submission submission, String year) {
        submissions.computeIfAbsent(authenticatedUser, (a) -> new SubmissionsForUser()).addForYear(year, submission);
    }

    public void debugDataset(AuthenticatedUser authenticatedUser) {
        submitNewTalkForYear(authenticatedUser, new Submission("Why Hibernate is Awezome!"), "2010");
        submitNewTalkForYear(authenticatedUser, new Submission("I still think Hibernate is awezome, even though my colleagues tease me!"), "2011");
        submitNewTalkForYear(authenticatedUser, new Submission("Well, now it's getting awkward, they really hate hibernate? WHY!?!"), "2012");
        submitNewTalkForYear(authenticatedUser, new Submission("Actually, hibernate kinda sucks!"), "2014");
        submitNewTalkForYear(authenticatedUser, new Submission("Databases? Pfft, that's sooo last year! Frontend is my new passion!"), "2016");
        submitNewTalkForYear(authenticatedUser, new Submission("React is the new Hibernate!"), "2016");
        submitNewTalkForYear(authenticatedUser, new Submission("Haaaaave you met Elm?!"), "2017");
    }
}
