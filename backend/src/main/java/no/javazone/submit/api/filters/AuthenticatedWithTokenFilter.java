package no.javazone.submit.api.filters;

import no.javazone.submit.api.representations.Token;
import no.javazone.submit.services.AuthenticationService;
import no.javazone.submit.api.session.AuthenticatedUser;
import no.javazone.submit.util.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.util.Optional;

import static no.javazone.submit.util.AuditLogger.Event.USER_AUTHENTICATION_OK;
import static no.javazone.submit.util.AuditLogger.Event.USER_AUTHENTICATION_WRONG_TOKEN;

@AuthenticatedWithToken
@Component
public class AuthenticatedWithTokenFilter implements ContainerRequestFilter {

    public static final String AUTHENTICATED_USER_PROPERTY = "submit.authenticated-user";

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticatedWithTokenFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Token token = new Token(requestContext.getHeaderString("X-token"));

        Optional<AuthenticatedUser> authenticatedUser = authenticationService.validateToken(token);

        if (authenticatedUser.isPresent()) {
            AuditLogger.log(USER_AUTHENTICATION_OK, "user " + authenticatedUser, "token " + token);
            requestContext.setProperty(AUTHENTICATED_USER_PROPERTY, authenticatedUser.get());
        } else {
            LOG.warn(String.format("Denied request due to invalid token. Token %s", token.toString()));
            AuditLogger.log(USER_AUTHENTICATION_WRONG_TOKEN, "token " + token);
            throw new ForbiddenException("Not valid token");
        }

    }
}
