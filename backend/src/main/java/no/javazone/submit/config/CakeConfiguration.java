package no.javazone.submit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cake")
public class CakeConfiguration {

    public String baseUri;

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }
}
