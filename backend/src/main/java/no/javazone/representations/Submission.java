package no.javazone.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.javazone.integrations.sleepingpill.model.get.Session;

import java.util.List;

import static java.util.stream.Collectors.toList;

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
    public List<Speaker> speakers;

    @SuppressWarnings("unused")
    private Submission() { }

    public Submission(String sessionId, String conferenceId, String status, String title, String theAbstract,
                      String intendedAudience, String format, String language, String outline, List<Speaker> speakers) {
        this.id = sessionId;
        this.conferenceId = conferenceId;
        this.status = status;
        this.title = title;
        this.theAbstract = theAbstract;
        this.intendedAudience = intendedAudience;
        this.format = format;
        this.language = language;
        this.outline = outline;
        this.speakers = speakers;
    }

    public static Submission fromSleepingPillSession(Session session) {
        return new Submission(
                session.sessionId,
                session.conferenceId,
                session.status.name(),
                session.getTitle(),
                session.getAbstract(),
                session.getIntendedAudience(),
                session.getFormat(),
                session.getLanguage(),
                session.getOutline(),
                session.speakers.stream().map(Speaker::fromSleepingPillSpeaker).collect(toList())
        );
    }

}
