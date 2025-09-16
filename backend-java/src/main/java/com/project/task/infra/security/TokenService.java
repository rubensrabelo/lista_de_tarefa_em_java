package com.project.task.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.project.task.models.User;

@Service
public class TokenService {
	
	private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String generateToken(User user) {
		try {
			logger.info("Generating token for user: {}", user.getEmail());
			
			Algorithm algorithm = Algorithm.HMAC256(secret);
			
			String token = JWT.create()
					.withIssuer("login-auth-api")
					.withSubject(user.getEmail())
					.withExpiresAt(this.generateExpirationDate())
					.sign(algorithm);	
			
			logger.info("Token generated successfully for user: {}", user.getEmail());
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException("Error while authenticating");
		}
	}

	public String validateToken(String token) {
		try {
			logger.info("Validating token.");
			
			
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String subject = JWT.require(algorithm)
					.withIssuer("login-auth-api")
					.build()
					.verify(token)
					.getSubject();
			logger.info("Token validated successfully. Subject: {}", subject);
			return subject;
		} catch (JWTVerificationException e) {
			return null;
		}
	}
	
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
