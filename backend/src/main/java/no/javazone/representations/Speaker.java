package no.javazone.representations;

public class Speaker {
    public String id;
    public String name;
    public String email;
    public String bio;

    public Speaker(String id, String name, String email, String bio) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
    }

    public static Speaker fromSleepingPillSpeaker(no.javazone.integrations.sleepingpill.model.Speaker speaker) {
        return new Speaker(
                speaker.id,
                speaker.name,
                speaker.email,
                speaker.getBio()
        );
    }
}
