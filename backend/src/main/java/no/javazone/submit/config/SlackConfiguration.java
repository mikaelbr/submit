package no.javazone.submit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "slack")
public class SlackConfiguration {

    public boolean enabled;

    public String token;

    public String channel;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
