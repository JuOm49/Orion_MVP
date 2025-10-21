package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.SubscriptionDto;
import com.openclassrooms.mddapi.exceptions.UnauthorizedException;
import com.openclassrooms.mddapi.security.services.AuthenticationService;
import com.openclassrooms.mddapi.services.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final AuthenticationService authenticationService;

    public SubscriptionController(SubscriptionService subscriptionService, AuthenticationService authenticationService) {
        this.subscriptionService = subscriptionService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/subscriptions/user")
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptionsForUser(HttpServletRequest request) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

        List<SubscriptionDto> subscriptionsDto = subscriptionService.getAllSubscriptionsForUser(userId);

        if(subscriptionsDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptionsDto);
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<Map<String, String>> subscribeToSubject(HttpServletRequest request, @RequestBody Long subjectId) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

        try {
            subscriptionService.subscribeToSubject(userId, subjectId);
        } catch (UnauthorizedException ignored) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to subscribe to this subject"));
        }

        return ResponseEntity.ok(Map.of("message", "Subscribed successfully"));
    }

    @DeleteMapping("/subscriptions/{subjectId}")
    public ResponseEntity<Void> unsubscribeFromSubject(HttpServletRequest request, @PathVariable Long subjectId) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

        subscriptionService.unsubscribeFromSubject(userId, subjectId);

        return ResponseEntity.noContent().build();
    }
}