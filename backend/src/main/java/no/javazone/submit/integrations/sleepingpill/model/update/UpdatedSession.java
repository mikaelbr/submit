package no.javazone.submit.integrations.sleepingpill.model.update;

import no.javazone.submit.integrations.sleepingpill.model.common.SessionData;
import no.javazone.submit.integrations.sleepingpill.model.common.SessionStatus;

import java.util.List;

public class UpdatedSession {

    public SessionData data;
    public SessionStatus status;
    public List<UpdatedSpeaker> speakers;

    @SuppressWarnings("unused")
    private UpdatedSession() { }

    public UpdatedSession(SessionStatus status, String title, String theAbstract, String intendedAudience,
                          String format, String language, String outline, String equipment, String length, String level, String suggestedKeywords, List<UpdatedSpeaker> speakers) {

        this.status = status;
        this.speakers = speakers;

        data = new SessionData();
        data.setTitle(title);
        data.setAbstract(theAbstract);
        data.setIntendedAudience(intendedAudience);
        data.setFormat(format);
        data.setLanguage(language);
        data.setOutline(outline);
        data.setEquipment(equipment);
        data.setLength(length);
        data.setLevel(level);
        data.setSuggestedKeywords(suggestedKeywords);
    }
}
