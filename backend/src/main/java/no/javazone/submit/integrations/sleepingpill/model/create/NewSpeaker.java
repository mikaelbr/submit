package no.javazone.submit.integrations.sleepingpill.model.create;

import no.javazone.submit.integrations.sleepingpill.model.common.SpeakerData;
import no.javazone.submit.api.representations.EmailAddress;

public class NewSpeaker {

    public String name;
    public String email;
    public SpeakerData data;

    @SuppressWarnings("unused")
    private NewSpeaker() { }

    public NewSpeaker(String name, String email, String bio, String zipCode) {
        this.name = name;
        this.email = email;

        data = new SpeakerData();
        data.setBio(bio);
        data.setZipCode(zipCode);
    }

    public static NewSpeaker draft(EmailAddress postedBy) {
        return new NewSpeaker(
                "",
                postedBy.toString(),
                "",
                ""
        );
    }
}
