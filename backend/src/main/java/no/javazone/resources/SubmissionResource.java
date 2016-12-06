package no.javazone.resources;

import no.javazone.representations.Submission;
import no.javazone.services.SubmissionService;
import no.javazone.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

@Path("/api/submissions")
@Component
public class SubmissionResource {

    private final SubmissionService submissionService;
    @Context
    private HttpServletRequest request;

    @Autowired
    public SubmissionResource(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllSubmissionsForLoggedInUser() {
        return SessionManager.getLoggedInUser(request)
                .map(authenticatedUser -> Response.ok(submissionService.getSubmissionsForUser(authenticatedUser)).build())
                .orElseGet(() -> Response.status(FORBIDDEN).build());
    }

    @GET
    @Path("/{submissionId}")
    @Produces(APPLICATION_JSON)
    public Response getSingleSubmissionsForLoggedInUser(@PathParam("submissionId") long submissionId) {
        return SessionManager.getLoggedInUser(request)
                .map(authenticatedUser -> Response.ok(submissionService.getSubmissionForUser(authenticatedUser, submissionId)).build())
                .orElseGet(() -> Response.status(FORBIDDEN).build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response newSubmission(Submission submission) {
        return SessionManager.getLoggedInUser(request)
                .map(authenticatedUser -> {
                    submissionService.submitNewTalk(authenticatedUser, submission);
                    return Response.ok().build();
                })
                .orElseGet(() -> Response.status(FORBIDDEN).build());
    }

}
