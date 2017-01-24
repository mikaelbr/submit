package no.javazone.submit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sleepingpill")
public class SleepingPillConfiguration {

    public String baseUri;
    public String username;
    public String password;
    public String activeYear;

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActiveYear(String activeYear) {
        this.activeYear = activeYear;
    }
}
