package no.javazone.submit.services;

import no.javazone.submit.dao.LoginTokenDao;
import no.javazone.submit.api.representations.EmailAddress;
import no.javazone.submit.api.representations.Token;
import no.javazone.submit.api.session.AuthenticatedUser;
import no.javazone.submit.util.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static no.javazone.submit.util.AuditLogger.Event.CREATE_TOKEN;
import static no.javazone.submit.util.AuditLogger.Event.REMOVE_TOKEN_FROM_BROWSER_LOCAL_STORAGE;

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
        AuditLogger.log(CREATE_TOKEN, "user " + email, "token " + token);
        return token;
    }

    public Optional<AuthenticatedUser> validateToken(Token token) {
        return loginTokenDao.getByToken(token)
                .map(t -> t.email)
                .map(AuthenticatedUser::new);
    }

    public void indicateLoginTokenRemovedFromBrowser(Token token) {
        LOG.info("Logging user out of browser for token " + token.toString() + ". Doesn't actually delete the token, as the user might use the link in the email again");
        AuditLogger.log(REMOVE_TOKEN_FROM_BROWSER_LOCAL_STORAGE, "token " + token);
        //loginTokenDao.removeToken(token);
    }
}
