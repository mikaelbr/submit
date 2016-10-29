package no.javazone.services;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthenticationService {

	// TODO: move this to persistent storage
	private Map<String, String> tokens = new HashMap<>();

	public String createTokenForEmail(String email) {
		String uuid = UUID.randomUUID().toString();
		tokens.put(email, uuid);
		return uuid;
	}
}
