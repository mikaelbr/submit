package no.javazone.resources;

import no.javazone.filters.AuthenticatedWithToken;
import no.javazone.services.SubmissionService;
import no.javazone.session.AuthenticatedUser;
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
import static no.javazone.filters.AuthenticatedWithTokenFilter.AUTHENTICATED_USER_PROPERTY;

@Path("/api/submissions")
@Component
@AuthenticatedWithToken
public class SubmissionResource {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final SubmissionService submissionService;

    @Context
    private ContainerRequestContext containerRequestContext;

    @Autowired
    public SubmissionResource(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAllSubmissionsForLoggedInUser() {
        return assertLoggedInUser(authenticatedUser ->
                Response.ok(submissionService.getSubmissionsForUser(authenticatedUser)).build()
        );
    }

    @GET
    @Path("/{submissionId}")
    @Produces(APPLICATION_JSON)
    public Response getSingleSubmissionsForLoggedInUser(@PathParam("submissionId") String submissionId) {
        return assertLoggedInUser(authenticatedUser ->
                Response.ok(submissionService.getSubmissionForUser(authenticatedUser, submissionId)).build()
        );
    }

    @POST
    @Produces(APPLICATION_JSON)
    public Response newDraft() {
        return assertLoggedInUser(authenticatedUser ->
                Response.ok(submissionService.createNewDraft(authenticatedUser)).build()
        );
    }

    private Response assertLoggedInUser(Function<AuthenticatedUser, Response> requestHandler) {
        return getAuthenticatedUser()
                .map(requestHandler)
                .orElseGet(() -> {
                    LOG.warn("No token even though we are in the resource method. Something is wrong. Did you forget to add the filter annotation to the Resource?");
                    return Response.status(FORBIDDEN).build();
                });
    }

    private Optional<AuthenticatedUser> getAuthenticatedUser() {
        return ofNullable((AuthenticatedUser) containerRequestContext.getProperty(AUTHENTICATED_USER_PROPERTY));
    }

}
