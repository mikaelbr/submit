package no.javazone.submit.integrations.sleepingpill.model.common;

import no.javazone.submit.api.representations.EmailAddress;

public class SessionComment {

    public String from;
    public String email;
    public String comment;

    @SuppressWarnings("unused")
    private SessionComment() { }

    public SessionComment(String from, EmailAddress emailAddress, String comment) {
	this.from = from;
	this.email = emailAddress.toString();
	this.comment = comment;
    }
}
