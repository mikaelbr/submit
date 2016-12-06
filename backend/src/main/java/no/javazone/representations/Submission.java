package no.javazone.representations;

public class Submission {

    public long id;
    public String title;

    @SuppressWarnings("unused")
    private Submission() { }

    public Submission(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
