package com.openclassrooms.mddapi.security.interceptors;

import com.openclassrooms.mddapi.security.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * HTTP interceptor for JWT-based authentication by user ID.
 * Validates Bearer tokens and extracts user ID for authenticated requests.
 */
@Component
public class AuthByIdInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;

    /**
     * Constructs a new AuthByIdInterceptor with the specified authentication service.
     *
     * @param authenticationService the service used for token validation and user extraction
     */
    public AuthByIdInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Intercepts HTTP requests to validate JWT authentication and extract user ID.
     *
     * @param request the HTTP request to process
     * @param response the HTTP response for setting error status
     * @param handler the handler object for the request (unused in this method)
     * @return true if authentication is successful and request should continue, false otherwise
     * @throws Exception if an error occurs during token processing
     */
    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        Long userId = authenticationService.handleUserFromToken(authHeader).getId();
        if(userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        } else {
            request.setAttribute("userId", userId);
            return true;
        }
    }
}
