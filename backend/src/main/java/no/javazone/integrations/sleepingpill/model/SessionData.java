package no.javazone.integrations.sleepingpill.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SessionData {

    public StringDataField title;
    // "abstract" is a reserved java word...
    @JsonProperty("abstract")
    public StringDataField theAbstract;
    public StringDataField intendedAudience;
    public StringDataField format;
    public StringDataField language;
    public StringDataField published;
    public StringDataField slug;
    public StringListDataField keywords;

    public StringListDataField tags;
    public StringDataField outline;



}
