package no.javazone.integrations.sleepingpill.model.common;

public class Speaker {

    public String id;
    public String name;
    public String email;
    public SpeakerData data;

    public String getBio() {
        return data.bio != null ? data.bio.value : null;
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
