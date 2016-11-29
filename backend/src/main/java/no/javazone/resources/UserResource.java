package no.javazone.resources;

import no.javazone.representations.EmailAddress;
import no.javazone.representations.Token;
import no.javazone.services.AuthenticationService;
import no.javazone.services.EmailService;
import no.javazone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/users")
@Component
public class UserResource {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;
    @Context
    private HttpServletRequest request;

    @Autowired
    public UserResource(AuthenticationService authenticationService, EmailService emailService) {
        this.authenticationService = authenticationService;
        this.emailService = emailService;
    }

    @POST
    @Path("/authtoken")
    public Response sendAuthenticationEmail(@QueryParam("email") EmailAddress email) {
        Token token = authenticationService.createTokenForEmail(email);
        emailService.sendTokenToUser(email, token);
        return Response.ok().build();
    }

    @POST
    @Path("/authtoken/use")
    public Response useAuthenticationEmail(@QueryParam("token") Token token) {
        return authenticationService.validateToken(token).map(user -> {
            SessionManager.login(request, user);
            return Response.ok().build();
        }).orElseGet(() -> Response.status(FORBIDDEN).build());
    }

    @POST
    @Path("/logout")
    public Response logout() {
        SessionManager.logout(request);
        return Response.ok().build();
    }

}
