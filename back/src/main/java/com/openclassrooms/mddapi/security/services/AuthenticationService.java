package com.openclassrooms.mddapi.security.services;

import com.openclassrooms.mddapi.DTO.LoginUserDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.services.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import com.openclassrooms.mddapi.exceptions.IllegalArgumentException;

import java.util.List;

@Service
public class AuthenticationService {

    private final JwtDecoder jwtDecoder;
    private final JWTService jwtService;
    private final UserService userService;

    public AuthenticationService(JwtDecoder jwtDecoder, JWTService jwtService, UserService userService) {
        this.jwtDecoder = jwtDecoder;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    /**
     * Handles authentication by creating an authentication token for the given user.
     * @param user
     * @return
     * @throws IllegalArgumentException if user is null
     * List.of() can be replaced with roles (ROLE_USER, ROLE_ADMIN) or authorities if needed
     * null credentials (password) in the token for security reasons
     */
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

    /**
     * Handles authentication by verifying user credentials and creating an authentication token.
     * @param user
     * @param loginUserDto
     * @return
     * @throws IllegalArgumentException if user is not found or credentials are incorrect
     * List.of() can be replaced with roles (ROLE_USER, ROLE_ADMIN) or authorities if needed
     * null credentials (password) in the token for security reasons
     */
    public Authentication handleUsernamePasswordAuthenticationToken(LoginUserDto loginUserDto, User user) {

        if(!userService.getPasswordEncoder().matches(loginUserDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("User not found or incorrect credentials.");
        }
        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                List.of()
        );
    }

    /**
     * Extracts the user from the JWT token present in the Authorization header.
     * @param authorizationHeader The Authorization header containing the Bearer token.
     * @return The User associated with the token.
     * @throws UsernameNotFoundException if the user email is not found in the token or if the user does not exist.
     */
    public User handleUserFromToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        Jwt jwt = this.jwtDecoder.decode(token);
        String userMail = this.jwtService.getUserMailFromToken(jwt);

        if (userMail == null) {
            throw new UsernameNotFoundException("User email not found in token");
        }

        return this.userService.findByEmail(userMail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Long getUserIdFromHttpServletRequest(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new IllegalArgumentException("User is not authenticated.");
        }
        return userId;
    }
}
