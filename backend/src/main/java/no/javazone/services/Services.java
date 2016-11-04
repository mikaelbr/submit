package no.javazone.services;

import io.dropwizard.setup.Environment;
import no.javazone.SubmitConfiguration;

public class Services {

    public final SubmitConfiguration configuration;
    public final Environment environment;
    public final EmailService emailService;
    public final AuthenticationService authenticationService;
    public final SubmissionService submissionService;

    public Services(SubmitConfiguration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
        emailService = new EmailService(configuration);
        authenticationService = new AuthenticationService();
        submissionService = new SubmissionService();
    }
}
