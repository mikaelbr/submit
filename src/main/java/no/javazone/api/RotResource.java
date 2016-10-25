package no.javazone.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class RotResource {
    @GET
    public Response get() {
        return Response.ok().entity("Her er rot").build();
    }
}
