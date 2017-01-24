package no.javazone.submit.services;

import no.javazone.submit.dao.LoginTokenDao;
import no.javazone.submit.api.representations.EmailAddress;
import no.javazone.submit.api.representations.Token;
import no.javazone.submit.api.session.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final LoginTokenDao loginTokenDao;

    @Autowired
    public AuthenticationService(LoginTokenDao loginTokenDao) {
        this.loginTokenDao = loginTokenDao;
    }

    public Token createTokenForEmail(EmailAddress email) {
        Token token = Token.generate();
        loginTokenDao.addLoginToken(email, token);
        LOG.info("Created token " + token + " for user " + email);
        return token;
    }

    public Optional<AuthenticatedUser> validateToken(Token token) {
        return loginTokenDao.getByToken(token)
                .map(t -> t.email)
                .map(AuthenticatedUser::new);
    }

    public void removeToken(Token token) {
        LOG.info("Removing token " + token.toString());
        loginTokenDao.removeToken(token);
    }
}
