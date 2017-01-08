package no.javazone.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sleepingpill")
public class SleepingPillConfiguration {

    public String baseUri;

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}
