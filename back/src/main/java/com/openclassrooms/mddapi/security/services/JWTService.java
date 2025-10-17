package com.openclassrooms.mddapi.security.services;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Data
@Service
public class JWTService {

    private final JwtEncoder jwtEncoder;

    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Generates a JWT token for the given authentication.
     * The token includes the issuer, issued at time, expiration time (1 day), and subject (username).
     *
     * @param authentication the authentication object containing user details
     * @return the generated JWT token as a string
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self").issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName()).build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        String token;
        try {
            token = this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }

        return token;
    }

    public String getUserMailFromToken(Jwt jwt) {
        return jwt.getSubject();
    }
}