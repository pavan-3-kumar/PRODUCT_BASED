package com.interview.auth.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    public String extractUsername(String token) {
    	return extractClaim(token ,Claims::getSubject);
    }
    public Date extractExpiration(String token) {
    	return extractClaim(token , Claims::getExpiration);
    }
    
	public <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
		Claims claims= extractAllclaims(token);
		// how this return the subject or getexpiration etc.. -> wrote in docs.
		return claimResolver.apply(claims);
	}

	public Claims extractAllclaims(String token) {
		return Jwts
                .parser()
                .verifyWith(getSignkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
	}

	public SecretKey getSignkey() {
		byte[] key = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(key);
	}
	
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	// we will get userDetails from the database
	public Boolean validateToken(String token , UserDetails userDetails) {
        final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	public String generateToken(String username) {
		Map<String,String> claims = new HashMap();
		return createToken(claims , username);
	}
	public String createToken(Map<String, String> claims, String username) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*1))
                .signWith(getSignkey(), SignatureAlgorithm.HS256).compact();
	}
}
