package no.javazone.representations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubmissionsForUser {

    public List<Year> years = new ArrayList<>();

    public void addForYear(String year, Submission submission) {
        // TODO (EHH): refactor this mess!
        Optional<Year> foundYear = years.stream().filter(y -> y.year.equals(year)).findFirst();
        if(foundYear.isPresent()) {
            foundYear.get().addSubmission(submission);
        } else {
            Year newYear = new Year(year);
            years.add(newYear);
            newYear.addSubmission(submission);
        }
    }
}
