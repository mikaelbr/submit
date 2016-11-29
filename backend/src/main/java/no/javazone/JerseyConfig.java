package no.javazone;

import no.javazone.resources.RootResource;
import no.javazone.resources.SubmissionResource;
import no.javazone.resources.UserResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(RootResource.class);
        register(SubmissionResource.class);
        register(UserResource.class);
    }

}
