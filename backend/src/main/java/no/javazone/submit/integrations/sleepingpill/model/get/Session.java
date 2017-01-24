package no.javazone.submit.integrations.sleepingpill.model.get;

import no.javazone.submit.integrations.sleepingpill.model.common.SessionData;
import no.javazone.submit.integrations.sleepingpill.model.common.SessionStatus;
import no.javazone.submit.integrations.sleepingpill.model.common.Speaker;

import java.util.List;

public class Session {

    public String sessionId;
    public String postedBy;
    public String conferenceId;
    public SessionStatus status;
    public List<Speaker> speakers;
    public SessionData data;

    public String getTitle() {
        return data.title != null ? data.title.value : null;
    }

    public String getAbstract() {
        return data.theAbstract != null ? data.theAbstract.value : null;
    }

    public String getIntendedAudience() {
        return data.intendedAudience != null ? data.intendedAudience.value : null;
    }

    public String getFormat() {
        return data.format != null ? data.format.value : null;
    }

    public String getLanguage() {
        return data.language != null ? data.language.value : null;
    }

    public String getEquipment() {
        return data.equipment != null ? data.equipment.value : null;
    }

    public String getLength() {
        return data.length != null ? data.length.value : null;
    }

    public String getLevel() {
        return data.level != null ? data.level.value : null;
    }

    public String getSuggestedKeywords() {
        return data.suggestedKeywords != null ? data.suggestedKeywords.value : null;
    }

    public String getInfoToProgramCommittee() {
        return data.infoToProgramCommittee != null ? data.infoToProgramCommittee.value : null;
    }

    /**
     * Legazy field, not used anymore
     */
    public String getPublished() {
        return data.published != null ? data.published.value : null;
    }

    /**
     * Legazy field, not used anymore
     */
    public List<String> getKeywords() {
        return data.keywords != null ? data.keywords.value : null;
    }

    public String getOutline() {
        return data.outline != null ? data.outline.value : null;
    }

    @Override
    public String toString() {
        return "Session{" +
                "sessionId='" + sessionId + '\'' +
                ", postedBy='" + postedBy + '\'' +
                ", conferenceId='" + conferenceId + '\'' +
                ", status=" + status +
                ", speakers=" + speakers +
                ", data=" + data +
                '}';
    }
}
