package no.javazone.filters;

import no.javazone.representations.Token;
import no.javazone.services.AuthenticationService;
import no.javazone.session.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.util.Optional;

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
            LOG.info(String.format("Checked the token against user service. User %s authenticated OK", authenticatedUser.get().emailAddress));
            requestContext.setProperty(AUTHENTICATED_USER_PROPERTY, authenticatedUser.get());
        } else {
            LOG.info(String.format("Denied request due to invalid token. Token %s", token.toString()));
            throw new ForbiddenException("Not valid token");
        }

    }
}
