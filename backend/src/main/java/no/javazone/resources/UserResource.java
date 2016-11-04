package no.javazone.resources;

import no.javazone.representations.EmailAddress;
import no.javazone.representations.Token;
import no.javazone.services.Services;
import no.javazone.session.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/users")
public class UserResource {

    @Context
    private HttpServletRequest request;

    private Services services;

    public UserResource(Services services) {
        this.services = services;
    }

    @GET
    @Path("/authtoken")
    public Response sendAuthenticationEmail(@QueryParam("email") EmailAddress email) {
        Token token = services.authenticationService.createTokenForEmail(email);
        services.emailService.sendTokenToUser(email, token);
        return Response.ok().build();
    }

    @GET
    @Path("/authtoken/use")
    public Response useAuthenticationEmail(@QueryParam("token") Token token) {
        return services.authenticationService.validateToken(token).map(user -> {
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
