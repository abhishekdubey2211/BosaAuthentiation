/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.jwtservice;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author Abhishek
 */
@Service
public class JWTUtil {

    static String JWT_SECRETE_KEY = "D75451ABAEF6820A42AF5D6C33722A9DB9288B61863CF22B2B4F5BBD3677DD847B97F3AEBFE2BAAC111ECBF103325639EE21E48025DD640109118257EAAD78B5";
    static long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    public String generteToken(UserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getAuthorities());
        claims.put("name", user.getUsername());
        claims.put("password", user.getPassword());
        try {
            return Jwts.builder()
                    .claims(claims)
                    .subject(user.getUsername())
                    .issuedAt(Date.from(Instant.now()))
                    .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                    .signWith(generteKey())
                    .compact();
            
        } catch (Exception e) {
        }
        return null;
    }

    private SecretKey generteKey() {
        byte[] decodedKey = Base64.getDecoder().decode(JWT_SECRETE_KEY);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser().verifyWith(generteKey())
                .build().parseSignedClaims(jwt).getPayload();
    }

    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    public boolean isTokenvalid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}
