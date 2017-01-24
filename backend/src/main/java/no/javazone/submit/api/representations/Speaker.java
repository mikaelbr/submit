package no.javazone.submit.api.representations;

public class Speaker {
    public String id;
    public String name;
    public String email;
    public String bio;
    public String zipCode;
    public String twitter;
    public boolean deletable;

    @SuppressWarnings("unused")
    private Speaker() { }

    public Speaker(String id, String name, String email, String bio, String zipCode, String twitter, boolean deletable) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.zipCode = zipCode;
        this.twitter = twitter;
        this.deletable = deletable;
    }

}
