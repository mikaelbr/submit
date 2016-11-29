package no.javazone;

import no.javazone.services.AuthenticationService;
import no.javazone.services.SubmissionService;
import no.javazone.session.AuthenticatedUser;
import org.springframework.stereotype.Component;

@Component
public class DebugData {

    private final AuthenticationService authenticationService;
    private final SubmissionService submissionService;

    public DebugData(AuthenticationService authenticationService, SubmissionService submissionService) {
        this.authenticationService = authenticationService;
        this.submissionService = submissionService;

        // TODO (EHH): We're loading debug data for now, should maybe be removed before we go live? :)
        initDebugData();
    }

    private void initDebugData() {
        AuthenticatedUser authenticatedUser = authenticationService.debugDataset();
        submissionService.debugDataset(authenticatedUser);
    }

}
