package com.wallet;

import java.util.Date;

import org.springframework.stereotype.Component;


import com.wallet.Entitis.CustomUserDetail;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class JwtTokenProvider {

    private final String JWT_SECRET = "phattien";


    private final long JWT_EXPIRATION = 604800000L;


    public String generateToken(CustomUserDetail userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }


    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(JWT_SECRET)
                            .parseClaimsJws(token)
                            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
           System.out.print("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
        	System.out.print("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
        	System.out.print("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
        	System.out.print("JWT claims string is empty.");
        }
        return false;
    }
}
