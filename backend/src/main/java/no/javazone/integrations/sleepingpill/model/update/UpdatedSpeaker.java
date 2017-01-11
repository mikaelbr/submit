package no.javazone.integrations.sleepingpill.model.update;

import no.javazone.integrations.sleepingpill.model.common.SpeakerData;
import no.javazone.representations.Speaker;

public class UpdatedSpeaker {

    public String id;
    public String name;
    public String email;
    public SpeakerData data;

    @SuppressWarnings("unused")
    private UpdatedSpeaker() { }

    public UpdatedSpeaker(String id, String name, String email, String bio) {
        this.id = id;
        this.name = name;
        this.email = email;

        data = new SpeakerData();
        data.setBio(bio);
    }

    public static UpdatedSpeaker fromApiObject(Speaker speaker) {
        return new UpdatedSpeaker(speaker.id, speaker.name, speaker.email, speaker.bio);
    }
}
