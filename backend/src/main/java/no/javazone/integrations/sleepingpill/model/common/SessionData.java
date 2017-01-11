package no.javazone.integrations.sleepingpill.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionData {

    public StringDataField title;
    // "abstract" is a reserved java word...
    @JsonProperty("abstract")
    public StringDataField theAbstract;
    public StringDataField format;
    public StringDataField intendedAudience;
    public StringDataField language;
    public StringDataField equipment;
    public StringDataField length;

    /**
     * Legazy field, not used anymore
     */
    public StringDataField published;
    /**
     * Legazy field, not used anymore
     */
    public StringDataField slug;
    /**
     * Legazy field, not used anymore
     */
    public StringListDataField keywords;

    public StringListDataField tags;
    public StringDataField outline;


    public void setTitle(String title) {
        this.title = new StringDataField(false, title);
    }

    public void setAbstract(String theAbstract) {
        this.theAbstract = new StringDataField(false, theAbstract);
    }

    public void setIntendedAudience(String intendedAudience) {
        this.intendedAudience = new StringDataField(false, intendedAudience);
    }

    public void setFormat(String format) {
        this.format = new StringDataField(false, format);
    }

    public void setLanguage(String language) {
        this.language = new StringDataField(false, language);
    }

    public void setOutline(String outline) {
        this.outline = new StringDataField(true, outline);
    }

    public void setEquipment(String equipment) {
        this.equipment = new StringDataField(true, equipment);
    }

    public void setLength(String length) {
        this.length = new StringDataField(false, length);
    }

    @Override
    public String toString() {
        return "SessionData{" +
                "title=" + title +
                ", theAbstract=" + theAbstract +
                ", format=" + format +
                ", intendedAudience=" + intendedAudience +
                ", language=" + language +
                ", equipment=" + equipment +
                ", length=" + length +
                ", published=" + published +
                ", slug=" + slug +
                ", keywords=" + keywords +
                ", tags=" + tags +
                ", outline=" + outline +
                '}';
    }
}
