package no.javazone.representations;

import java.util.ArrayList;
import java.util.List;

public class Year {

    public String year;
    public List<Submission> submissions;

    public Year(String year) {
        this(year, new ArrayList<>());
    }

    public Year(String year, List<Submission> submissions) {
        this.year = year;
        this.submissions = submissions;
    }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
    }
}
