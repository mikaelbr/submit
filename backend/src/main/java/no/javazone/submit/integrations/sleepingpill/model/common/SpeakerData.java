package no.javazone.submit.integrations.sleepingpill.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpeakerData {

    public StringDataField bio;
    @JsonProperty("zip-code")
    public StringDataField zipCode;
    public StringDataField twitter;
    public StringDataField pictureId;

    public void setBio(String bio) {
        this.bio = new StringDataField(false, bio);
    }

    public void setZipCode(String zipCode) {
        this.zipCode = new StringDataField(true, zipCode);
    }

    public void setTwitter(String twitter) {
        this.twitter = new StringDataField(false, twitter);
    }

    public void setPictureId(String pictureId) {
        this.pictureId = new StringDataField(false, pictureId);
    }

    @Override
    public String toString() {
        return "SpeakerData{" +
                "bio=" + bio +
                ", zipCode=" + zipCode +
                ", twitter=" + twitter +
                ", pictureId=" + pictureId +
                '}';
    }
}
