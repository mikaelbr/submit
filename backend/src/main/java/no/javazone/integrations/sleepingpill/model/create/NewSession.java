package no.javazone.integrations.sleepingpill.model.create;

import no.javazone.integrations.sleepingpill.model.common.SessionData;
import no.javazone.integrations.sleepingpill.model.common.SessionStatus;

import java.util.List;

public class NewSession {

    public List<NewSpeaker> speakers;
    public SessionData data;
    public SessionStatus status;
    public String postedBy;

    @SuppressWarnings("unused")
    private NewSession() { }

    public NewSession(SessionStatus status, String postedBy, String title, String theAbstract, String intendedAudience,
                      String format, String language, String outline, List<NewSpeaker> speakers) {

        this.status = status;
        this.postedBy = postedBy;
        this.speakers = speakers;

        data = new SessionData();
        data.setTitle(title);
        data.setAbstract(theAbstract);
        data.setIntendedAudience(intendedAudience);
        data.setFormat(format);
        data.setLanguage(language);
        data.setOutline(outline);
    }
}
