package no.javazone.submit.api.session;

import no.javazone.submit.api.representations.EmailAddress;

import java.util.Objects;

public class AuthenticatedUser {

    public final EmailAddress emailAddress;

    public AuthenticatedUser(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return emailAddress.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthenticatedUser that = (AuthenticatedUser) o;
        return Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress);
    }
}
