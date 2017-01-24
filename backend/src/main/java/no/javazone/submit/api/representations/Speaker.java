package no.javazone.submit.api.representations;

public class Speaker {
    public String id;
    public String name;
    public String email;
    public String bio;
    public String zipCode;
    public String twitter;

    @SuppressWarnings("unused")
    private Speaker() { }

    public Speaker(String id, String name, String email, String bio, String zipCode, String twitter) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.zipCode = zipCode;
        this.twitter = twitter;
    }

    public static Speaker fromSleepingPillSpeaker(no.javazone.submit.integrations.sleepingpill.model.common.Speaker speaker) {
        return new Speaker(
                speaker.id,
                speaker.name,
                speaker.email,
                speaker.getBio(),
                speaker.getZipCode(),
                speaker.getTwitter()
        );
    }

}
