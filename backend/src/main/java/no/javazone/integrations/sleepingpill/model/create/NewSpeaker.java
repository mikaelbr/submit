package no.javazone.integrations.sleepingpill.model.create;

import no.javazone.integrations.sleepingpill.model.common.SpeakerData;

public class NewSpeaker {

    public String name;
    public String email;
    public SpeakerData data;

    @SuppressWarnings("unused")
    private NewSpeaker() { }

    public NewSpeaker(String name, String email, String bio) {
        this.name = name;
        this.email = email;

        data = new SpeakerData();
        data.setBio(bio);
    }
}
