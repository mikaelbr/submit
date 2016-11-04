package no.javazone.resources;

import no.javazone.representations.EmailAddress;
import no.javazone.representations.Token;
import no.javazone.services.Services;
import no.javazone.session.AuthenticatedUser;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/users")
public class UserResource {

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
		Optional<AuthenticatedUser> authenticatedUser = services.authenticationService.validateToken(token);
		if(authenticatedUser.isPresent()) {
			// TODO (EHH): Init session
			return Response.ok().build();
		} else {
			return Response.status(FORBIDDEN).build();
		}
	}

}
