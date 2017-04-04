package no.javazone.submit.api.resources;

import no.javazone.submit.api.filters.AuthenticatedWithToken;
import no.javazone.submit.api.representations.Comment;
import no.javazone.submit.api.representations.Submission;
import no.javazone.submit.api.session.AuthenticatedUser;
import no.javazone.submit.services.SubmissionService;
import no.javazone.submit.util.AuditLogger;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static no.javazone.submit.api.filters.AuthenticatedWithTokenFilter.AUTHENTICATED_USER_PROPERTY;
import static no.javazone.submit.util.AuditLogger.Event.WRONG_ACCESS_TOKEN_IN_RESOURCE;

@Path("/api/submissions")
@Component
public class SubmissionResource {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final SubmissionService submissionService;

    @Autowired
    public SubmissionResource(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GET
    @AuthenticatedWithToken
    @Produces(APPLICATION_JSON)
    public Response getAllSubmissionsForLoggedInUser(@Context ContainerRequestContext context) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.getSubmissionsForUser(authenticatedUser)).build()
        );
    }

    @GET
    @AuthenticatedWithToken
    @Path("/{submissionId}")
    @Produces(APPLICATION_JSON)
    public Response getSingleSubmissionsForLoggedInUser(@Context ContainerRequestContext context,
                                                        @PathParam("submissionId") String submissionId) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.getSubmissionForUser(authenticatedUser, submissionId)).build()
        );
    }

    @POST
    @AuthenticatedWithToken
    @Produces(APPLICATION_JSON)
    public Response newDraft(@Context ContainerRequestContext context) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.createNewDraft(authenticatedUser)).build()
        );
    }

    @PUT
    @AuthenticatedWithToken
    @Path("/{submissionId}")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response updateSubmission(@Context ContainerRequestContext context,
                                     @PathParam("submissionId") String submissionId,
                                     Submission submission) {
        return assertLoggedInUser(context, authenticatedUser ->
		Response.ok(submissionService.updateSubmission(authenticatedUser, submissionId, submission, empty())).build()
        );
    }

    @POST
    @AuthenticatedWithToken
    @Path("/{submissionId}/speakers/{speakerId}/picture")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(APPLICATION_JSON)
    public Response addPictureToSpeaker(@Context ContainerRequestContext context,
                                        @PathParam("submissionId") String submissionId,
                                        @PathParam("speakerId") String speakerId,
                                        @FormDataParam("image") byte[] image,
                                        @FormDataParam("image") FormDataBodyPart picture) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.addPictureToSpeaker(authenticatedUser, submissionId, speakerId, image, picture.getMediaType().toString())).build());
    }

    @GET
    @Path("/{submissionId}/speakers/{speakerId}/picture")
    public Response getSpeakerPicture(@Context ContainerRequestContext context,
                                        @PathParam("submissionId") String submissionId,
                                        @PathParam("speakerId") String speakerId) {
        // We don't authenticate picture requests, due to problems adding headers on such requests from browser
        return Response
                .ok(submissionService.getSpeakerPicture(submissionId, speakerId))
                .header("Content-Type", "image/jpeg")
                .build();
    }

    @POST
    @AuthenticatedWithToken
    @Path("/{submissionId}/comments")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public Response postComment(@Context ContainerRequestContext context,
                                @PathParam("submissionId") String submissionId,
                                Comment comment) {
        return assertLoggedInUser(context, authenticatedUser ->
                Response.ok(submissionService.postComment(authenticatedUser, submissionId, comment)).build()
        );
    }

    private Response assertLoggedInUser(ContainerRequestContext context, Function<AuthenticatedUser, Response> requestHandler) {
        return getAuthenticatedUser(context)
                .map(requestHandler)
                .orElseGet(() -> {
                    LOG.warn("No token even though we are in the resource method. Something is wrong. Did you forget to add the filter annotation to the Resource?");
                    AuditLogger.log(WRONG_ACCESS_TOKEN_IN_RESOURCE);
                    return Response.status(FORBIDDEN).build();
                });
    }

    private Optional<AuthenticatedUser> getAuthenticatedUser(ContainerRequestContext context) {
        return ofNullable((AuthenticatedUser) context.getProperty(AUTHENTICATED_USER_PROPERTY));
    }

}
