package no.javazone.submit.integrations.sleepingpill.model.get;

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

    public String getSlugFromId(String id) {
        return conferences.stream()
                .filter(c -> c.id.equals(id))
                .map(c -> c.slug)
                .findAny()
                .orElse("unknown");
    }

    public String getIdFromSlug(String slug) {
        return conferences.stream()
                .filter(c -> c.slug.equals(slug))
                .map(c -> c.id)
                .findAny()
                .orElse("unknown");
    }
}
