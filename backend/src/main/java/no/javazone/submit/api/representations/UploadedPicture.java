package no.javazone.submit.api.representations;

public class UploadedPicture {

    public String pictureUrl;

    @SuppressWarnings("unused")
    private UploadedPicture() { }

    public UploadedPicture(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
