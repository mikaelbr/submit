package no.javazone.representations;

import java.util.Objects;
import java.util.UUID;

public class Token {

	private String token;

	public Token(String token) {
		this.token = token;
	}

	public static Token generate() {
		return new Token(UUID.randomUUID().toString());
	}

	@Override
	public String toString() {
		return token;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Token token1 = (Token) o;
		return Objects.equals(token, token1.token);
	}

	@Override
	public int hashCode() {
		return Objects.hash(token);
	}
}
