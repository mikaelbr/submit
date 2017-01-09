package no.javazone.integrations.sleepingpill.model.create;

import no.javazone.integrations.sleepingpill.model.common.SessionData;
import no.javazone.integrations.sleepingpill.model.common.SessionStatus;
import no.javazone.representations.EmailAddress;

import java.util.List;

import static java.util.Collections.singletonList;

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

    public String getPublished() {
        return data.published != null ? data.published.value : null;
    }

    public List<String> getKeywords() {
        return data.keywords != null ? data.keywords.value : null;
    }

    public String getOutline() {
        return data.outline != null ? data.outline.value : null;
    }

    public static NewSession draft(EmailAddress postedBy) {
        return new NewSession(
                SessionStatus.DRAFT,
                postedBy.toString(),
                "Draft",
                "",
                "",
                "",
                "",
                "",
                singletonList(NewSpeaker.draft(postedBy))
        );
    }
}
