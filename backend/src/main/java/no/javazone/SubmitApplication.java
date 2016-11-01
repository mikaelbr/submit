package no.javazone;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import no.javazone.filters.CorsFilter;
import no.javazone.resources.RootResource;
import no.javazone.resources.UserResource;

import java.util.List;

import static java.util.Arrays.asList;

public class SubmitApplication extends Application<SubmitConfiguration> {
    public static void main(String[] args) throws Exception {
        System.out.println("Starter submit");
        if (args.length == 0) {
            args = new String[]{"server", "backend/configuration.yaml"};
        }
        new SubmitApplication().run(args);
    }

    @Override
    public void run(SubmitConfiguration configuration, Environment environment) throws Exception {
        resources(configuration).forEach(resource -> environment.jersey().register(resource));
        filters(configuration).forEach(filter -> environment.jersey().register(filter));
    }

    private List<Object> resources(SubmitConfiguration configuration) {
        return asList(
                new RootResource(),
                new UserResource(configuration)
        );
    }

    private List<Object> filters(SubmitConfiguration configuration) {
        return asList(
                new CorsFilter()
        );
    }
}
