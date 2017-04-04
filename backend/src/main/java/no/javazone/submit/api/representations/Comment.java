package no.javazone.submit.api.representations;

public class Comment {
    public String name;
    public String comment;

    @SuppressWarnings("unused")
    private Comment() { }

    public Comment(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }
}
