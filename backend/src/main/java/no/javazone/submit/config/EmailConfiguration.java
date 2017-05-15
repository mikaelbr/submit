package no.javazone.submit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfiguration {

    public boolean enableSmtp;

    public String smtpUser;

    public String smtpPass;

    public boolean enableSendgrid;

    public String sendgridApikey;

    public String tokenLinkPrefix;

    public String subjectPrefix;

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public void setSmtpPass(String smtpPass) {
        this.smtpPass = smtpPass;
    }

    public void setTokenLinkPrefix(String tokenLinkPrefix) {
        this.tokenLinkPrefix = tokenLinkPrefix;
    }

    public void setSubjectPrefix(String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }

    public void setEnableSmtp(boolean enableSmtp) {
	this.enableSmtp = enableSmtp;
    }

    public void setEnableSendgrid(boolean enableSendgrid) {
	this.enableSendgrid = enableSendgrid;
    }

    public void setSendgridApikey(String sendgridApikey) {
	this.sendgridApikey = sendgridApikey;
    }
}
