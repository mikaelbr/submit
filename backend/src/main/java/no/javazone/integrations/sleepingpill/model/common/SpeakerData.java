package no.javazone.integrations.sleepingpill.model.common;

public class SpeakerData {

    public StringDataField bio;

    public void setBio(String bio) {
        this.bio = new StringDataField(false, bio);
    }

    @Override
    public String toString() {
        return "SpeakerData{" +
                "bio=" + bio +
                '}';
    }
}
