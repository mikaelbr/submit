package no.javazone.resources;

import no.javazone.services.Services;
import no.javazone.session.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/submissions")
public class SubmissionResource {

    @Context
    private HttpServletRequest request;

    private Services services;

    public SubmissionResource(Services services) {

        this.services = services;
    }

    @GET
    public Response getAllSubmissionsForLoggedInUser() {
        return SessionManager.getLoggedInUser(request).map(authenticatedUser -> {
            return Response.ok(services.submissionService.getSubmissionsForUser(authenticatedUser)).build();
        }).orElseGet(() -> Response.status(FORBIDDEN).build());
    }

}
