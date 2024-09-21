/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bosa.jwtservice;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.json.simple.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Main {

    public static void main(String[] args) {
        JWTTokenGenerator jwtTokenGenerator = new JWTTokenGenerator();
        String token = jwtTokenGenerator.generateToken();
        if (token != null) {
            jwtTokenGenerator.verifyToken(token);
        } else {
            System.err.println("Failed to generate secret key. Exiting...");
        }
    }
}

class JWTTokenGenerator {

    public static String secretKey;

    public String generateToken() {
        secretKey = generateSecretKey();
        System.out.println("Genereted key :: >> "+secretKey);
        if (secretKey != null) {
            // Other configuration values
            String token;
            Map<String, Object> tokenMap = new HashMap<>();
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1); // 1 minute from now

            // Use the generated secret key with the JWT signing algorithm
            Algorithm algorithm = Algorithm.HMAC512(secretKey);

            // Generate JWT token
            token = JWT.create()
                    .withPayload(tokenMap)
                    .withClaim("token", "token")
                    .withClaim("token_secret", "secret_token")
                    .withIssuedAt(date) // Set issued at time
                    .withSubject("Meeting Join")
                    .withIssuer("Abhishek")
                    .withExpiresAt(calendar.getTime()) // 1 hour from now
                    .sign(algorithm);
            System.out.println("JWT Token: " + token);
            return token;
        } else {
            return null;
        }
    }

    public void verifyToken(String token) {
        try {
            // Decode and verify the token
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secretKey))
                    .withIssuer("Abhishek") // Must match the issuer used when generating the token
                    .withSubject("Meeting Join")
                    .build();
            DecodedJWT jwt = verifier.verify(token);

            // Format dates
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String issuedAt = dateFormat.format(jwt.getIssuedAt());
            String expiresAt = dateFormat.format(jwt.getExpiresAt());

            // Additional details to retrieve
            String header = jwt.getHeader();
            String signature = jwt.getSignature();

            // Retrieve additional claims
            Map<String, Claim> claims = jwt.getClaims();
            JSONObject additionalClaims = new JSONObject();
            for (Map.Entry<String, Claim> entry : claims.entrySet()) {
                additionalClaims.put(entry.getKey(), entry.getValue().asString());
            }

            // Construct JSON object with all details
            JSONObject data = new JSONObject();
            data.put("Subject", jwt.getSubject());
            data.put("Issuer", jwt.getIssuer());
            data.put("Issued At", issuedAt);
            data.put("Expires At", expiresAt);
            data.put("Token", jwt.getClaim("token").asString());
            data.put("Token Secret", jwt.getClaim("token_secret").asString());
            data.put("Header", header);
            data.put("Signature", signature);
            data.put("Signature key", secretKey);
            data.put("Algorithm", jwt.getAlgorithm());
            data.put("token", token);
            data.put("Additional Claims", additionalClaims);

            // Print out the JSON object
            System.out.println("Decoded JWT Token:");
            System.out.println(data.toJSONString());
        } catch (Exception ex) {
            System.err.println("Failed to verify token: " + ex.getMessage());
        }
    }


    public static String generateSecretKey(String algorithm) {
        try {
            // Initialize KeyGenerator
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);

            // Generate a secret key
            SecretKey secretKey = keyGenerator.generateKey();

            // Convert the secret key to a Base64-encoded string
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Algorithm not supported: " + e.getMessage());
            return null;
        }
    }

    private String generateSecretKey() {
        // Create a KeyGenerator instance for the desired algorithm (HMAC SHA-256)
        KeyGenerator keyGenerator;
        try {
//            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator = KeyGenerator.getInstance("HmacSHA512");
            // Initialize the KeyGenerator with a secure random number generator
            keyGenerator.init(256); // Key size for HMAC SHA-256 should be 256 bits

            // Generate the secret key
            SecretKey secretKey = keyGenerator.generateKey();

            // Encode the secret key in Base64 for storage or transmission
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
/* 
Genereted key :: >> JWF9N8//wLCNgzZUo21MXsjDOSKsocezFQCc2DiROfc=
JWT Token: eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbiI6InRva2VuIiwidG9rZW5fc2VjcmV0Ijoic2VjcmV0X3Rva2VuIiwiaWF0IjoxNzE5NzQ3NzA4LCJzdWIiOiJNZWV0aW5nIEpvaW4iLCJpc3MiOiJBYmhpc2hlayIsImV4cCI6MTcxOTc0Nzc2OH0.itu9fac2KNQF-JgYo5xg1Cv5mQDzCVZEOtpzQQ-giP1F3PohaPP_Jcz6zDw0rMGYA4u4HifxHHNVYoXEg1tjvA

Decoded JWT Token:
{
  "Expires At": "2024-06-30 17:12:48",
  "Issued At": "2024-06-30 17:11:48",
  "Header": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9",
  "Issuer": "Abhishek",
  "Additional Claims": {
    "token_secret": "secret_token",
    "sub": "Meeting Join",
    "iss": "Abhishek",
    "exp": null,
    "iat": null,
    "token": "token"
  },
  "Signature key": "JWF9N8//wLCNgzZUo21MXsjDOSKsocezFQCc2DiROfc=",
  "Signature": "itu9fac2KNQF-JgYo5xg1Cv5mQDzCVZEOtpzQQ-giP1F3PohaPP_Jcz6zDw0rMGYA4u4HifxHHNVYoXEg1tjvA",
  "Token Secret": "secret_token",
  "Token": "token",
  "Algorithm": "HS512",
  "Subject": "Meeting Join",
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbiI6InRva2VuIiwidG9rZW5fc2VjcmV0Ijoic2VjcmV0X3Rva2VuIiwiaWF0IjoxNzE5NzQ3NzA4LCJzdWIiOiJNZWV0aW5nIEpvaW4iLCJpc3MiOiJBYmhpc2hlayIsImV4cCI6MTcxOTc0Nzc2OH0.itu9fac2KNQF-JgYo5xg1Cv5mQDzCVZEOtpzQQ-giP1F3PohaPP_Jcz6zDw0rMGYA4u4HifxHHNVYoXEg1tjvA"


*/
