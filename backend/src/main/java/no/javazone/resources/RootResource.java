package no.javazone.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class RootResource {
    @GET
    public Response get() {
        return Response.ok().entity("Her er rot").build();
    }
}
