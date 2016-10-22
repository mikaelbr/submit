package no.javazone.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/forslag")
public class ForslagResource {

    @GET
    public Response get() {
        return Response.ok().entity("hei hei").build();
    }
}
