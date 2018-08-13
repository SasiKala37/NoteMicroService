package com.bridgelabz.notemicroservice.configuration;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTokenParser {

	private JWTokenParser() {

	}

	private static final String KEY = "sasi";

	public static Claims parseJwt(String jwt) {

		return Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwt).getBody();
	}

}
