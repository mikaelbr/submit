package no.javazone.representations;

public class Submission {

    public String id;
    public String title;

    @SuppressWarnings("unused")
    private Submission() { }

    public Submission(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
