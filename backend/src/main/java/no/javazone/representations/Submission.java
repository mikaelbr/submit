package no.javazone.representations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Submission {

    public String id;
    public String conferenceId;
    public String status;
    public String title;
    @JsonProperty("abstract")
    public String theAbstract;
    public String intendedAudience;

    public String format;

    public String language;
    public String outline;
    public String equipment;
    public String length;
    public List<Speaker> speakers;
    public boolean editable;

    @SuppressWarnings("unused")
    private Submission() { }

    public Submission(String sessionId, String conferenceId, String status, String title, String theAbstract,
                      String intendedAudience, String format, String language, String outline, String equipment, String length, List<Speaker> speakers, boolean editable) {
        this.id = sessionId;
        this.conferenceId = conferenceId;
        this.status = status;
        this.title = title;
        this.theAbstract = theAbstract;
        this.intendedAudience = intendedAudience;
        this.format = format;
        this.language = language;
        this.outline = outline;
        this.equipment = equipment;
        this.length = length;
        this.speakers = speakers;
        this.editable = editable;
    }

}
