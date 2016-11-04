package no.javazone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import no.javazone.filters.CorsFilter;
import no.javazone.resources.RootResource;
import no.javazone.resources.SubmissionResource;
import no.javazone.resources.UserResource;
import no.javazone.services.Services;
import no.javazone.session.AuthenticatedUser;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;
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
        objectmapper(environment);

        debugDataset(services);
    }

    private void debugDataset(Services services) {
        AuthenticatedUser authenticatedUser = services.authenticationService.debugDataset();
        services.submissionService.debugDataset(authenticatedUser);
    }

    private void objectmapper(Environment environment) {
        environment.getObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .disable(MapperFeature.AUTO_DETECT_GETTERS)
                .disable(MapperFeature.AUTO_DETECT_SETTERS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Oslo")))
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setVisibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.ANY);
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
