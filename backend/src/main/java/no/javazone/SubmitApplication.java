package no.javazone;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import no.javazone.filters.CorsFilter;
import no.javazone.resources.RootResource;
import no.javazone.resources.SubmissionResource;
import no.javazone.resources.UserResource;
import no.javazone.services.Services;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

public class SubmitApplication extends Application<SubmitConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(SubmitApplication.class);

    public static void main(String[] args) throws Exception {
        System.out.println("Starter submit");
        if (args.length == 0) {
            args = new String[]{"server", "configuration.yaml"};
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
        sessionHandler(environment);
    }

    private void sessionHandler(Environment environment) {
        final HashSessionManager manager = new HashSessionManager();
        manager.setSessionIdPathParameterName("none");
        manager.getSessionCookieConfig().setName("_SUBMIT_SESSION");
        manager.getSessionCookieConfig().setHttpOnly(true);
        manager.getSessionCookieConfig().setSecure(false);
        manager.getSessionCookieConfig().setPath("/");
        manager.setMaxInactiveInterval((int) TimeUnit.DAYS.toSeconds(365));
        environment.servlets().setSessionHandler(new SessionHandler(manager));
    }

    private List<Object> resources(Services services) {
        return asList(
                new RootResource(),
                new UserResource(services),
                new SubmissionResource(services)
        );
    }

    private List<Object> filters(Services services) {
        return asList(
                new CorsFilter()
        );
    }
}
