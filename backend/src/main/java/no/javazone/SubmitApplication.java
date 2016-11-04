package no.javazone;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import no.javazone.filters.CorsFilter;
import no.javazone.resources.RootResource;
import no.javazone.resources.UserResource;
import no.javazone.services.Services;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static java.util.Arrays.asList;

public class SubmitApplication extends Application<SubmitConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(SubmitApplication.class);

    public static void main(String[] args) throws Exception {
        System.out.println("Starter submit");
        if (args.length == 0) {
            args = new String[]{"server", "backend/configuration.yaml"};
        }
        new SubmitApplication().run(args);

        LOG.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        LOG.info("@@@@@@@@@@@@@@@@@ STARTUP OK @@@@@@@@@@@@@@@@@@@");
        LOG.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }

    @Override
    public void run(SubmitConfiguration configuration, Environment environment) throws Exception {
        Services services = new Services(configuration, environment);
        resources(services).forEach(resource -> environment.jersey().register(resource));
        filters(services).forEach(filter -> environment.jersey().register(filter));
    }

    private List<Object> resources(Services services) {
        return asList(
                new RootResource(),
                new UserResource(services)
        );
    }

    private List<Object> filters(Services services) {
        return asList(
                new CorsFilter()
        );
    }
}
