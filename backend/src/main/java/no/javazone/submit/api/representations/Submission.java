package no.javazone.submit.api.representations;

import com.fasterxml.jackson.annotation.JsonProperty;
import no.javazone.submit.util.AuditLogger;

import javax.ws.rs.ForbiddenException;
import java.util.List;

import static no.javazone.submit.util.AuditLogger.Event.INVALID_TALK_FIELD;

public class Submission {

    public static final int MAX_FIELD_LENGTH_TO_AVOID_DOS_ATTACK = 100_000;
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
    public String level;
    public String suggestedKeywords;
    public String infoToProgramCommittee;
    public List<Speaker> speakers;
    public boolean editable;

    @SuppressWarnings("unused")
    private Submission() { }

    public Submission(String sessionId, String conferenceId, String status, String title, String theAbstract,
                      String intendedAudience, String format, String language, String outline, String equipment, String length, String level,
                      String suggestedKeywords, String infoToProgramCommittee, List<Speaker> speakers, boolean editable) {
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
        this.level = level;
        this.suggestedKeywords = suggestedKeywords;
        this.infoToProgramCommittee = infoToProgramCommittee;
        this.speakers = speakers;
        this.editable = editable;
    }

    public void validate() {
        // We don't validate the actual values, that happens in the frontend.
        // If the speaker messes with the data directly, they can thank themselves
        // that the talk is mangled, and probably won't be chosen... :P
        checkFieldLength(status);
        checkFieldLength(title);
        checkFieldLength(theAbstract);
        checkFieldLength(intendedAudience);
        checkFieldLength(format);
        checkFieldLength(language);
        checkFieldLength(outline);
        checkFieldLength(equipment);
        checkFieldLength(length);
        checkFieldLength(level);
        checkFieldLength(suggestedKeywords);
        checkFieldLength(infoToProgramCommittee);
        speakers.forEach(s -> {
            checkFieldLength(s.name);
            checkFieldLength(s.bio);
            checkFieldLength(s.email);
            checkFieldLength(s.id);
            checkFieldLength(s.twitter);
            checkFieldLength(s.zipCode);
        });
    }

    private void checkFieldLength(String fieldData) {
        if(fieldData != null && fieldData.length() > MAX_FIELD_LENGTH_TO_AVOID_DOS_ATTACK) {
            AuditLogger.log(INVALID_TALK_FIELD, "fielddata " + fieldData);
            throw new ForbiddenException("Field data too long: " + fieldData);
        }
    }
}
