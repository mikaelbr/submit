package no.javazone.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public class SessionManager {

    private static final Logger LOG = LoggerFactory.getLogger(SessionManager.class);

    private static final String AUTHENTICATED_USER_SESSION_KEY = "authenticatedUser";

    public static void login(HttpServletRequest request, AuthenticatedUser authenticatedUser) {
        HttpSession session = request.getSession();
        LOG.info("Logged in user " + authenticatedUser + " to session " + session.getId());
        session.setAttribute(AUTHENTICATED_USER_SESSION_KEY, authenticatedUser);
    }

    public static void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        LOG.info("Logged out user with session id " + session.getId());
        session.invalidate();
    }

    public static Optional<AuthenticatedUser> getLoggedInUser(HttpServletRequest request) {
        return ofNullable(request.getSession(false))
                .map(s -> (AuthenticatedUser) s.getAttribute(AUTHENTICATED_USER_SESSION_KEY));
    }
}
