package no.javazone.submit.api.resources;

import no.javazone.submit.api.filters.AuthenticatedWithToken;
import no.javazone.submit.api.representations.Submission;
import no.javazone.submit.services.SubmissionService;
import no.javazone.submit.api.session.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static no.javazone.submit.api.filters.AuthenticatedWithTokenFilter.AUTHENTICATED_USER_PROPERTY;

@Path("/api/submissions")
@Component
@AuthenticatedWithToken
public class SubmissionResource {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionResource(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllSubmissionsForLoggedInUser(@Context ContainerRequestContext context) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.getSubmissionsForUser(authenticatedUser)).build()
        );
    }

    @GET
    @Path("/{submissionId}")
    @Produces(APPLICATION_JSON)
    public Response getSingleSubmissionsForLoggedInUser(@Context ContainerRequestContext context,
                                                        @PathParam("submissionId") String submissionId) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.getSubmissionForUser(authenticatedUser, submissionId)).build()
        );
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response newDraft(@Context ContainerRequestContext context) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.createNewDraft(authenticatedUser)).build()
        );
    }

    @PUT
    @Path("/{submissionId}")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response updateSubmission(@Context ContainerRequestContext context,
                                     @PathParam("submissionId") String submissionId,
                                     Submission submission) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.updateSubmission(authenticatedUser, submissionId, submission)).build()
        );
    }

    private Response assertLoggedInUser(ContainerRequestContext context, Function<AuthenticatedUser, Response> requestHandler) {
        return getAuthenticatedUser(context)
                .map(requestHandler)
                .orElseGet(() -> {
                    LOG.warn("No token even though we are in the resource method. Something is wrong. Did you forget to add the filter annotation to the Resource?");
                    return Response.status(FORBIDDEN).build();
                });
    }

    private Optional<AuthenticatedUser> getAuthenticatedUser(ContainerRequestContext context) {
        return ofNullable((AuthenticatedUser) context.getProperty(AUTHENTICATED_USER_PROPERTY));
    }

}
