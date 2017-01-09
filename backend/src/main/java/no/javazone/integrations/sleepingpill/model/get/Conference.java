package no.javazone.integrations.sleepingpill.model.get;

public class Conference {

    public String id;
    public String slug;
    public String name;

    @Override
    public String toString() {
        return "Conference{" +
                "id='" + id + '\'' +
                ", slug='" + slug + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
