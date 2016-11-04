package no.javazone.representations;

import java.util.ArrayList;
import java.util.List;

public class Year {

    public List<Submission> submissions = new ArrayList<>();

    public void addSubmission(Submission submission) {
        submissions.add(submission);
    }
}
