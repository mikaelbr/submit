package no.javazone.submit.integrations.sleepingpill.model.common;

public class Speaker {

    public String id;
    public String name;
    public String email;
    public SpeakerData data;

    public String getBio() {
        return data.bio != null ? data.bio.value : null;
    }

    public String getZipCode() {
        return data.zipCode != null ? data.zipCode.value : null;
    }

    public String getTwitter() {
        return data.twitter != null ? data.twitter.value : null;
    }

    public String getPictureId() {
        return data.pictureId != null ? data.pictureId.value : null;
    }

    @Override
    public String toString() {
        return "Speaker{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", data=" + data +
                '}';
    }
}
