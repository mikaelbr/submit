package no.javazone.submit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "token")
public class TokenConfig {

    public boolean enabled;

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

}
