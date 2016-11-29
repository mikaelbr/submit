package no.javazone.services;

import no.javazone.representations.EmailAddress;
import no.javazone.representations.Token;
import no.javazone.session.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Service
public class AuthenticationService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    // TODO: move this to persistent storage
    private Map<Token, EmailAddress> tokens = new HashMap<>();

    public AuthenticationService() {
        // TODO (EHH): Remove debug data sometime :)
        debugDataset();
    }

    public Token createTokenForEmail(EmailAddress email) {
        Token token = Token.generate();
        tokens.put(token, email);
        LOG.info("Created token " + token + " for user " + email);
        return token;
    }

    public Optional<AuthenticatedUser> validateToken(Token token) {
        return ofNullable(tokens.getOrDefault(token, null)).map(AuthenticatedUser::new);
    }

    public AuthenticatedUser debugDataset() {
        Token token = new Token("test-token");
        tokens.put(token, new EmailAddress("example@example.com"));
        return validateToken(token).get();
    }
}
