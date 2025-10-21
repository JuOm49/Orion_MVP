package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.LoginUserDto;
import com.openclassrooms.mddapi.DTO.RegisterUserDto;
import com.openclassrooms.mddapi.DTO.UserDto;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.security.services.AuthenticationService;
import com.openclassrooms.mddapi.security.services.JWTService;
import com.openclassrooms.mddapi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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

        String token = this.jwtService.generateToken(authentication);

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(500).body(Map.of("error", "Token generation failed"));
        }

        return ResponseEntity.ok(Map.of( "token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginUserDto loginUserDto) {
        User user = userService.findByEmail(loginUserDto.getIdentifier())
                .or(() -> userService.findByName(loginUserDto.getIdentifier()))
                .orElse(null);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        Authentication authentication = this.authenticationService.handleUsernamePasswordAuthenticationToken(loginUserDto, user);

        String token = this.jwtService.generateToken(authentication);

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(500).body(Map.of("error", "Token generation failed"));
        }

        return ResponseEntity.ok(Map.of( "token", token));
    }

    @PutMapping("/user")
    public ResponseEntity<Map<String, String>> updateUser(HttpServletRequest request, @RequestBody RegisterUserDto registerUserDto) {
        Long userId = (Long) request.getAttribute("userId");

        if(userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        User updatedUser = userService.update(userId, registerUserDto);

        if(updatedUser == null) {
            return ResponseEntity.status(400).body(Map.of("error", "Could not update user"));
        }

        Authentication authentication = this.authenticationService.handleUsernamePasswordAuthenticationToken(updatedUser);

        String token = this.jwtService.generateToken(authentication);

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(500).body(Map.of("error", "Token generation failed"));
        }

        return ResponseEntity.ok(Map.of( "token", token));
    }

    @GetMapping("/currentUser")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

       String userEmail = authenticationService.handleUserFromToken(authorizationHeader).getEmail();

        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        UserDto userDto = userService.convertUserToUserDto(user);

        return ResponseEntity.ok(userDto);
    }

}
