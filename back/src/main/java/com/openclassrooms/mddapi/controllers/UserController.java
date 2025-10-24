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

import java.util.Map;

/**
 * REST controller for managing user accounts and authentication.
 * Provides endpoints for user registration, login, profile updates, and user information retrieval.
 * Some endpoints require authentication handled by AuthByIdInterceptor.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;

    /**
     * Constructs a new UserController with the specified services.
     *
     * @param userService the service for user operations
     * @param jwtService the service for JWT token generation and validation
     * @param authenticationService the service for authentication operations
     */
    public UserController(UserService userService, JWTService jwtService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    /**
     * Registers a new user account.
     *
     * @param registerUserDto the user registration data containing username, email, and password
     * @return ResponseEntity containing JWT token on successful registration or error message
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterUserDto registerUserDto) {
         User newUser = userService.save(registerUserDto);

         Authentication authentication = this.authenticationService.handleUsernamePasswordAuthenticationToken(newUser);

        return tokenResponse(authentication);
    }

    /**
     * Authenticates a user with email/username and password.
     *
     * @param loginUserDto the login credentials containing identifier (email or username) and password
     * @return ResponseEntity containing JWT token on successful authentication or 401 Unauthorized with error message
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginUserDto loginUserDto) {
        User user = userService.findByEmail(loginUserDto.getIdentifier())
                .or(() -> userService.findByName(loginUserDto.getIdentifier()))
                .orElse(null);

        if(user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        Authentication authentication = this.authenticationService.handleUsernamePasswordAuthenticationToken(loginUserDto, user);

        return tokenResponse(authentication);
    }

    /**
     * Updates the authenticated user's profile information.
     *
     * @param request the HTTP request with user ID set by AuthByIdInterceptor after JWT validation
     * @param registerUserDto the updated user data containing new username, email, and/or password
     * @return ResponseEntity containing new JWT token on successful update or error message
     *         Returns 401 Unauthorized if token is invalid
     *         Returns 400 Bad Request if update fails
     */
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

        return tokenResponse(authentication);
    }

    /**
     * Return the current authenticated user's information.
     *
     * @param authorizationHeader the Bearer token in Authorization header for user identification
     * @return ResponseEntity containing user information or error status
     *         Returns 401 Unauthorized if token is missing or invalid
     *         Returns 404 Not Found if user doesn't exist
     */
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

    // method to generate standardized JWT token response with jwtService.
    private ResponseEntity<Map<String, String>> tokenResponse(Authentication authentication) {
        String token = this.jwtService.generateToken(authentication);

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(500).body(Map.of("error", "Token generation failed"));
        }

        return ResponseEntity.ok(Map.of( "token", token));
    }
}
