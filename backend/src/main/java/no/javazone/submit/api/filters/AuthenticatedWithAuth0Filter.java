package no.javazone.submit.api.filters;

import com.auth0.jwt.interfaces.DecodedJWT;
import no.javazone.submit.api.representations.EmailAddress;
import no.javazone.submit.api.session.AuthenticatedUser;
import no.javazone.submit.config.Auth0Config;
import no.javazone.submit.integrations.auth0.Auth0Service;
import no.javazone.submit.util.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static no.javazone.submit.util.AuditLogger.Event.USER_AUTHENTICATION_OK;

@AuthenticatedWithAuth0
@Component
public class AuthenticatedWithAuth0Filter implements ContainerRequestFilter {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    public static final String AUTHENTICATED_USER_PROPERTY = "submit.authenticated-user";

    private Auth0Config auth0Config;
    private final Auth0Service service;

    @Autowired
    public AuthenticatedWithAuth0Filter(Auth0Config auth0Config, Auth0Service service) {
        this.auth0Config = auth0Config;
        this.service = service;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!auth0Config.enabled) {
            return;
        }
        DecodedJWT jwt = getToken(requestContext.getHeaderString("authorization"))
                .flatMap(service::verify)
                .orElseThrow(ForbiddenException::new);

        List<String> emails = jwt.getClaim("emails").asList(String.class);
        if (emails.size() != 1) {
            LOG.warn("List of emails had not a single unique entry... Emails: " + emails);
        }
        String email = emails.get(0);
        LOG.info("CLAIM: " + email);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(new EmailAddress(email));

        AuditLogger.log(USER_AUTHENTICATION_OK, "user " + authenticatedUser, "token auth0");
        requestContext.setProperty(AUTHENTICATED_USER_PROPERTY, authenticatedUser);
    }

    private Optional<String> getToken(String authHeader) {
        if (authHeader == null) {
            LOG.info("No authorization header found");
            return Optional.empty();
        }

        String token = authHeader.replace("Bearer ", "");
        return Optional.of(token);
    }

}
