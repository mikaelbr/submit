package no.javazone.dao;

import no.javazone.representations.EmailAddress;
import no.javazone.representations.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginTokenDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LoginTokenDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addLoginToken(EmailAddress email, Token token) {
        jdbcTemplate.update("INSERT INTO login_token (email, token, created) VALUES (?, ?, now())",
                email.toString(), token.toString());
    }

    public Optional<LoginToken> getByToken(Token token) {
        try {
            return Optional.of(jdbcTemplate.queryForObject("SELECT id, email, token, created FROM login_token WHERE token = ?", rowmapper(), token.toString()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void removeToken(Token token) {
        jdbcTemplate.update("DELETE FROM login_token where token = ?", token.toString());
    }

    private RowMapper<LoginToken> rowmapper() {
        return (rs, i) -> new LoginToken(
                rs.getLong("id"),
                new EmailAddress(rs.getString("email")),
                new Token(rs.getString("token"))
        );
    }

    public class LoginToken {

        public final long id;
        public final EmailAddress email;
        public final Token token;

        public LoginToken(long id, EmailAddress email, Token token) {
            this.id = id;
            this.email = email;
            this.token = token;
        }
    }

}
