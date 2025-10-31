package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.SubscriptionDto;
import com.openclassrooms.mddapi.security.services.AuthenticationService;
import com.openclassrooms.mddapi.services.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing user subscriptions to subjects.
 * Provides endpoints for retrieving, creating, and deleting subscriptions.
 * All endpoints require authentication handled by AuthByIdInterceptor.
 */
@RestController
@RequestMapping("/api")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final AuthenticationService authenticationService;

    /**
     * Constructs a new SubscriptionController with the specified services.
     *
     * @param subscriptionService the service for subscription operations
     * @param authenticationService the service responsible for authentication management,
     *                              used to extract the user ID from the HTTP request with
     *                              {@code getUserIdFromHttpServletRequest()}
     */
    public SubscriptionController(SubscriptionService subscriptionService, AuthenticationService authenticationService) {
        this.subscriptionService = subscriptionService;
        this.authenticationService = authenticationService;
    }

    /**
     * Return all subscriptions for the authenticated user.
     *
     * @param request the HTTP request with user ID set by AuthByIdInterceptor after JWT validation
     * @return ResponseEntity containing a list of user subscriptions or 204 No Content if no subscriptions found
     */
    @GetMapping("/subscriptions/user")
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptionsForUser(HttpServletRequest request) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

        List<SubscriptionDto> subscriptionsDto = subscriptionService.getAllSubscriptionsForUser(userId);

        if(subscriptionsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptionsDto);
    }

    /**
     * Subscribes the authenticated user to a specific subject.
     *
     * @param request the HTTP request with user ID set by AuthByIdInterceptor after JWT validation
     * @param subjectId the ID of the subject to subscribe to
     * @return ResponseEntity containing success message or error details
     *         Returns 403 Forbidden if user is not authorized to subscribe to the subject
     */
    @PostMapping("/subscriptions")
    public ResponseEntity<Map<String, String>> subscribeToSubject(HttpServletRequest request, @RequestBody Long subjectId) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

        try {
            subscriptionService.subscribeToSubject(userId, subjectId);
        } catch (IllegalArgumentException ignored) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to subscribe to this subject"));
        }

        return ResponseEntity.ok(Map.of("message", "Subscribed successfully"));
    }

    /**
     * Unsubscribes the authenticated user from a specific subject.
     *
     * @param request the HTTP request with user ID set by AuthByIdInterceptor after JWT validation
     * @param subjectId the ID of the subject to unsubscribe from
     * @return ResponseEntity with 204 No Content status indicating successful unsubscription
     */
    @DeleteMapping("/subscriptions/{subjectId}")
    public ResponseEntity<Void> unsubscribeFromSubject(HttpServletRequest request, @PathVariable Long subjectId) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

        subscriptionService.unsubscribeFromSubject(userId, subjectId);

        return ResponseEntity.noContent().build();
    }
}