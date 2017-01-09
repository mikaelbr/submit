package no.javazone.representations;

import no.javazone.integrations.sleepingpill.model.create.NewSpeaker;

public class Speaker {
    public String id;
    public String name;
    public String email;
    public String bio;

    @SuppressWarnings("unused")
    private Speaker() { }

    public Speaker(String id, String name, String email, String bio) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.bio = bio;
    }

    public static Speaker fromSleepingPillSpeaker(no.javazone.integrations.sleepingpill.model.common.Speaker speaker) {
        return new Speaker(
                speaker.id,
                speaker.name,
                speaker.email,
                speaker.getBio()
        );
    }

    public static Speaker fromSleepingPillNewSpeaker(NewSpeaker speaker) {
        return new Speaker(
                null,
                speaker.name,
                speaker.email,
                speaker.getBio()
        );
    }
}
