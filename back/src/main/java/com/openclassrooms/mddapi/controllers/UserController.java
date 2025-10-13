package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.LoginUserDto;
import com.openclassrooms.mddapi.DTO.RegisterUserDto;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.security.services.AuthenticationService;
import com.openclassrooms.mddapi.security.services.JWTService;
import com.openclassrooms.mddapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;


    public UserController(UserService userService, JWTService jwtService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterUserDto registerUserDto) {
         User newUser = userService.save(registerUserDto);
         Authentication authentication = this.authenticationService.handleUsernamePasswordAuthenticationToken(newUser);

        return ResponseEntity.ok(Map.of( "token", this.jwtService.generateToken(authentication)));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginUserDto loginUserDto) {
        User user = userService.findByEmail(loginUserDto.getIdentifier())
                .or(() -> userService.findByName(loginUserDto.getIdentifier()))
                .orElse(null);

        if(user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        Authentication authentication = this.authenticationService.handleUsernamePasswordAuthenticationToken(loginUserDto, user);

        return ResponseEntity.ok(Map.of( "token", jwtService.generateToken(authentication)));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<Map<String, String>> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Missing or invalid Authorization header"));
        }

       String userEmail = authenticationService.handleUserFromToken(authorizationHeader).getEmail();

        User user = userService.findByEmail(userEmail).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId().toString(),
                "name", user.getName(),
                "email", user.getEmail()
        ));
    }
}
