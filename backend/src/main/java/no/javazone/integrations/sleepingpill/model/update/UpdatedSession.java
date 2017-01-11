package no.javazone.integrations.sleepingpill.model.update;

import no.javazone.integrations.sleepingpill.model.common.SessionData;
import no.javazone.integrations.sleepingpill.model.common.SessionStatus;

import java.util.List;

public class UpdatedSession {

    public SessionData data;
    public SessionStatus status;
    public List<UpdatedSpeaker> speakers;

    @SuppressWarnings("unused")
    private UpdatedSession() { }

    public UpdatedSession(SessionStatus status, String title, String theAbstract, String intendedAudience,
                          String format, String language, String outline, List<UpdatedSpeaker> speakers) {

        this.status = status;
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
