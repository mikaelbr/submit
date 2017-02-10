package no.javazone.submit.integrations.sleepingpill.model.update;

import no.javazone.submit.api.representations.Speaker;
import no.javazone.submit.integrations.sleepingpill.model.common.SpeakerData;

public class UpdatedSpeaker {

    public String id;
    public String name;
    public String email;
    public SpeakerData data;

    @SuppressWarnings("unused")
    private UpdatedSpeaker() {
    }

    public UpdatedSpeaker(String id, String name, String email, String bio, String zipCode, String twitter, String pictureId) {
        if(id != null && !id.isEmpty()) {
            this.id = id;
        }
        this.name = name;
        this.email = email;

        data = new SpeakerData();
        data.setBio(bio);
        data.setZipCode(zipCode);
        data.setTwitter(twitter);
        if (pictureId != null) {
            data.setPictureId(pictureId);
        }
    }

    public static UpdatedSpeaker fromApiObject(Speaker speaker) {
        return new UpdatedSpeaker(speaker.id, speaker.name, speaker.email, speaker.bio, speaker.zipCode, speaker.twitter, speaker.pictureId);
    }
}
