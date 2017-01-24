package no.javazone.submit.api.resources;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/api/")
@Component
public class RootResource {
    @GET
    public Response get() {
        return Response.ok().entity("Her er rot").build();
    }
}
