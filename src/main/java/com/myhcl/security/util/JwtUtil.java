package com.myhcl.security.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${app.secret}")
	private String secretKey;
	
	//Generate the JWT token
	public String generateToken(String username) {
		return Jwts.builder()
                .setSubject(username)  // Set username as subject
                .setIssuer("Sandeep-Verma")
                .setIssuedAt(new Date(System.currentTimeMillis())) // Set issued date
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5))) // Expiration time
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()),SignatureAlgorithm.HS256) // Sign with key
                .compact();
	}
	
	// Extract Username from Token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
 // Validate token
    public boolean validateToken(String token,String username) {
    	String tokenUsername=extractUsername(token);
    	return (username.equals(tokenUsername) && !isTokenExpired(token));
    	
    }
    
 // Extract Expiration Date from Token
    public Date getExpirationDate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
    
    //  Check if Token is Expired
    public boolean isTokenExpired(String token) {
        try {
            return getExpirationDate(token).before(new Date(System.currentTimeMillis())); // If expiration date is before current date, token is expired
        } catch (ExpiredJwtException ex) {
            return true; // Token has already expired
        } catch (JwtException ex) {
            return false; // Invalid token
        }
    }

}
