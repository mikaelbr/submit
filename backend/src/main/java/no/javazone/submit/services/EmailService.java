package no.javazone.submit.services;

import com.sendgrid.*;
import no.javazone.submit.api.representations.EmailAddress;
import no.javazone.submit.api.representations.Submission;
import no.javazone.submit.api.representations.Token;
import no.javazone.submit.config.EmailConfiguration;
import no.javazone.submit.util.AuditLogger;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

import static java.util.stream.Collectors.joining;
import static no.javazone.submit.util.AuditLogger.Event.SENT_EMAIL;

@Service
public class EmailService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final EmailConfiguration emailConfiguration;
    private final AuthenticationService authenticationService;

    @Autowired
    public EmailService(EmailConfiguration emailConfiguration, AuthenticationService authenticationService) {
        this.emailConfiguration = emailConfiguration;
        this.authenticationService = authenticationService;
    }

    public void sendTokenToUser(EmailAddress emailAddress, Token token) {
        send(emailAddress, emailConfiguration.subjectPrefix + "Talk submission login", generateTokenEmail(token));
    }

    public void notifySpeakerAboutStatusChangeToInReview(Submission submission) {
        submission.speakers.stream()
                .map(s -> s.email)
                .map(EmailAddress::new)
                .filter(Objects::nonNull)
                .forEach(emailAddress -> {
                    Token token = authenticationService.createTokenForEmail(emailAddress);
                    send(emailAddress, emailConfiguration.subjectPrefix + "Foredrag sendt inn: " + submission.title, generateReviewEmail(submission, token));
                });
    }

    private void send(EmailAddress address, String subject, String emailBody) {
	if (emailConfiguration.enableSmtp) {
	    sendViaSmtp(address, subject, emailBody);
	} else if (emailConfiguration.enableSendgrid) {
	    sendViaSendGrid(address, subject, emailBody);
	} else {
	    log(address, subject, emailBody);
	}
	AuditLogger.log(SENT_EMAIL, "emailaddress " + address);
    }

    private void sendViaSmtp(EmailAddress address, String subject, String emailBody) {
        try {
            Email email = new SimpleEmail();
            email.setHostName("smtp.googlemail.com");
            email.setSmtpPort(465);
            email.setAuthenticator(new DefaultAuthenticator(emailConfiguration.smtpUser, emailConfiguration.smtpPass));
            email.setSSLOnConnect(true);
            email.setFrom("no-reply@example.com", "Program Committee");
            email.setSubject(subject);
            email.setMsg(emailBody);
            email.addTo(address.toString());
            email.send();
        } catch (EmailException e) {
            LOG.warn("Couldn't send email to " + address, e);
        }
    }

    private void sendViaSendGrid(EmailAddress address, String subject, String emailBody) {
	com.sendgrid.Email from = new com.sendgrid.Email("no-reply@bekk.no", "BEKK Program Committee");
	com.sendgrid.Email to = new com.sendgrid.Email(address.toString());
	Content content = new Content("text/plain", emailBody);
	Mail mail = new Mail(from, subject, to, content);

	SendGrid sg = new SendGrid(emailConfiguration.sendgridApikey);
	Request request = new Request();
	try {
	    request.method = Method.POST;
	    request.endpoint = "mail/send";
	    request.body = mail.build();
	    Response response = sg.api(request);
	    System.out.println(response.statusCode);
	    System.out.println(response.body);
	    System.out.println(response.headers);
	} catch (IOException e) {
	    LOG.warn("Couldn't send email to " + address, e);
	}
    }

    private void log(EmailAddress address, String subject, String emailBody) {
	LOG.info(String.format("Did not send email since no email sender is configured. Address: %s, Subject: %s, Body: %s", address, subject, emailBody));
    }

    private String generateTokenEmail(Token token) {
        StringBuilder b = new StringBuilder();
        b.append("Ready to submit or edit your talk?\n\n");
        b.append("Use this link to log your browser in to our submission system:\n");
        b.append(emailConfiguration.tokenLinkPrefix).append("/").append(token).append("\n\n");
        b.append("Clicking this link will authenticate your browser and keep you logged in. Using a public computer? Use the 'forget me' button on the logged in page.").append("\n\n");
        b.append("Best regards,").append("\n").append("The BEKK Program Committee");
        return b.toString();
    }

    private String generateReviewEmail(Submission submission, Token token) {
        String speakerNames = submission.speakers.stream()
                .map(s -> s.name)
                .filter(Objects::nonNull)
                .filter((s) -> !s.isEmpty())
                .collect(joining(" & "));

        StringBuilder b = new StringBuilder();
        b.append("Hei " + speakerNames + "\n\n");
        b.append("Dette er en kjapp bekreftelse på at du har markert ditt foredrag '" + submission.title + "' som klar for gjennomgang :)\n\n");
        b.append("Du kan fortsatt redigere foredraget, men etter at innsendelsesfristen er utløpt eller foredraget er godkjent bør du begrense deg til å kun gjøre mindre endringer. Rediger foredraget her: \n");
        b.append(emailConfiguration.tokenLinkPrefix).append("\n\n");
        b.append("Vennlig hilsen,").append("\n").append("fagdag-komiteen");
        return b.toString();
    }
}
