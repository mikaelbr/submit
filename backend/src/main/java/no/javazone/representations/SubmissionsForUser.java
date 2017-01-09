package no.javazone.representations;

import java.util.ArrayList;
import java.util.List;

public class SubmissionsForUser {

    public List<Year> years = new ArrayList<>();

    public SubmissionsForUser(List<Year> years) {
        this.years = years;
    }

}
