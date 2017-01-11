package no.javazone.integrations.sleepingpill.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpeakerData {

    public StringDataField bio;
    @JsonProperty("zip-code")
    public StringDataField zipCode;

    public void setBio(String bio) {
        this.bio = new StringDataField(false, bio);
    }

    public void setZipCode(String zipCode) {
        this.zipCode = new StringDataField(true, zipCode);
    }

    @Override
    public String toString() {
        return "SpeakerData{" +
                "bio=" + bio +
                ", zipCode=" + zipCode +
                '}';
    }
}
