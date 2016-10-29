package no.javazone.resources;

import no.javazone.SubmitConfiguration;
import no.javazone.services.AuthenticationService;
import no.javazone.services.EmailService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/users")
public class UserResource {

	private SubmitConfiguration configuration;

	public UserResource(SubmitConfiguration configuration) {
		this.configuration = configuration;
	}

	@GET
	@Path("/authtoken")
	public Response sendAuthenticationEmail(@QueryParam("email") String email) {
		String token = new AuthenticationService().createTokenForEmail(email);
		new EmailService(configuration).sendTokenToUser(email, token);
		return Response.ok().build();
	}

}
