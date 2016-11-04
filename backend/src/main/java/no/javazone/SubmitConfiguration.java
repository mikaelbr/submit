package no.javazone;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class SubmitConfiguration extends Configuration {

    @JsonProperty
    public String smtpUser;

    @JsonProperty
    public String smtpPass;

    @JsonProperty
    public String tokenLinkPrefix;

}
