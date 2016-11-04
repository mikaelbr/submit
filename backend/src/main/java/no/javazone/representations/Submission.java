package no.javazone.representations;

public class Submission {

    public String title;

    @SuppressWarnings("unused")
    private Submission() { }

    public Submission(String title) {
        this.title = title;
    }
}
