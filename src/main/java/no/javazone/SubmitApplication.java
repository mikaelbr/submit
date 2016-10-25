package no.javazone;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import no.javazone.api.ForslagResource;

public class SubmitApplication extends Application<SubmitConfiguration> {
    public static void main(String[] args) throws Exception {
        System.out.println("Starter submit");
        if (args.length == 0) {
            args = new String[]{"server", "configuration.yaml"};
        }
        new SubmitApplication().run(args);
    }

    @Override
    public void run(SubmitConfiguration submitConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new ForslagResource());
    }
}
