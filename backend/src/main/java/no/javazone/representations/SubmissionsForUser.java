package no.javazone.representations;

import java.util.HashMap;
import java.util.Map;

public class SubmissionsForUser {

    public Map<String, Year> years = new HashMap<>();

    public void addForYear(String year, Submission submission) {
        years.computeIfAbsent(year, (y) -> new Year()).addSubmission(submission);
    }
}
