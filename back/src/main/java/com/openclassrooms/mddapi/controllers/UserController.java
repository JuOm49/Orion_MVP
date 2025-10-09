package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.RegisterUserDto;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    //à supprimer lorsque ce sera dans JWTService
    private final JwtEncoder jwtEncoder;

    public UserController(UserService userService, JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterUserDto registerUserDto) {
         User newUser = userService.save(registerUserDto);

         Authentication authentication = handleUsernamePasswordAuthenticationToken(newUser);

        return ResponseEntity.ok(generateToken(authentication));
    }


    //à mettre prochainement dans un service de sécurité authenticationService dans le package services
    public Authentication handleUsernamePasswordAuthenticationToken(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User not found or incorrect credentials.");
        }
        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of()
        );
    }

    //JWTService à créer et supprimer cette méthode de UserController
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self").issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName()).build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

}
