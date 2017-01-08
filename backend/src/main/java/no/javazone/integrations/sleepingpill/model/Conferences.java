package no.javazone.integrations.sleepingpill.model;

import java.util.List;

public class Conferences {

    public List<Conference> conferences;

    public String getNameFromId(String id) {
        return conferences.stream()
                .filter(c -> c.id.equals(id))
                .map(c -> c.name)
                .findAny()
                .orElse("unknown");
    }
}
