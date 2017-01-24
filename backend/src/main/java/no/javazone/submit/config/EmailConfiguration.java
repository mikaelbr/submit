package no.javazone.submit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfiguration {

    public String smtpUser;

    public String smtpPass;

    public String tokenLinkPrefix;

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public void setSmtpPass(String smtpPass) {
        this.smtpPass = smtpPass;
    }

    public void setTokenLinkPrefix(String tokenLinkPrefix) {
        this.tokenLinkPrefix = tokenLinkPrefix;
    }
}
