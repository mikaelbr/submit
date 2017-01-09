package no.javazone.integrations.sleepingpill.model.update;

import no.javazone.integrations.sleepingpill.model.common.SessionData;
import no.javazone.integrations.sleepingpill.model.common.SessionStatus;

public class UpdatedSession {

    public SessionData data;
    public SessionStatus status;

    @SuppressWarnings("unused")
    private UpdatedSession() { }

    public UpdatedSession(SessionStatus status, String title, String theAbstract, String intendedAudience,
                          String format, String language, String outline) {

        this.status = status;

        data = new SessionData();
        data.setTitle(title);
        data.setAbstract(theAbstract);
        data.setIntendedAudience(intendedAudience);
        data.setFormat(format);
        data.setLanguage(language);
        data.setOutline(outline);
    }
}
