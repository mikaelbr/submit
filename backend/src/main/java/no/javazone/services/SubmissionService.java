package no.javazone.services;

import no.javazone.representations.Submission;
import no.javazone.session.AuthenticatedUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class SubmissionService {

    // TODO (EHH): Integrate with sleepingpill instead of in memory store...
    private Map<AuthenticatedUser, List<Submission>> submissions = new HashMap<>();

    public List<Submission> getSubmissionsForUser(AuthenticatedUser authenticatedUser) {
        return submissions.getOrDefault(authenticatedUser, emptyList());
    }
}
