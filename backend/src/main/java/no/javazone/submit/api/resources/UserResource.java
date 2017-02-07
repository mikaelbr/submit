package no.javazone.submit.api.resources;

import no.javazone.submit.api.representations.EmailAddress;
import no.javazone.submit.api.representations.Token;
import no.javazone.submit.services.AuthenticationService;
import no.javazone.submit.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/api/users")
@Component
public class UserResource {

    private final AuthenticationService authenticationService;
    private final EmailService emailService;

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

    @DELETE
    @Path("/authtoken")
    public void deletetoken(@QueryParam("token") Token token) {
        authenticationService.removeToken(token);
    }

}
