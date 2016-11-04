package no.javazone.session;

import no.javazone.representations.EmailAddress;

public class AuthenticatedUser {

	private EmailAddress emailAddress;

	public AuthenticatedUser(EmailAddress emailAddress) {
		this.emailAddress = emailAddress;
	}
}
