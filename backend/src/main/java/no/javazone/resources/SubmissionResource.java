package no.javazone.resources;

import no.javazone.representations.Submission;
import no.javazone.services.Services;
import no.javazone.session.SessionManager;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
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
    @Produces(APPLICATION_JSON)
    public Response getAllSubmissionsForLoggedInUser() {
        return SessionManager.getLoggedInUser(request)
                .map(authenticatedUser -> Response.ok(services.submissionService.getSubmissionsForUser(authenticatedUser)).build())
                .orElseGet(() -> Response.status(FORBIDDEN).build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response newSubmission(Submission submission) {
        return SessionManager.getLoggedInUser(request)
                .map(authenticatedUser -> {
                    services.submissionService.submitNewTalk(authenticatedUser, submission);
                    return Response.ok().build();
                })
                .orElseGet(() -> Response.status(FORBIDDEN).build());
    }

}
