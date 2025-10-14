package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.SubscriptionDto;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.User;
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


    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/subscriptions/user")
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptionsForUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        if(userId == null) {
            return ResponseEntity.status(401).build();
        }

        List<SubscriptionDto> subscriptionDtos = subscriptionService.getAllSubscriptionsForUser(userId);

        if(subscriptionDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(subscriptionDtos);
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<Map<String, String>> subscribeToSubject(HttpServletRequest request, @RequestBody Long subjectId) {
        Long userId = (Long) request.getAttribute("userId");
        
        if(userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid token"));
        }

        User user = new User();
        user.setId(userId);

        Subject subject = new Subject();
        subject.setId(subjectId);

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubject(subject);

        SubscriptionDto newSubscriptionDto = subscriptionService.subscribeToSubject(subscription);

        if (newSubscriptionDto.getId() == null) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to create subscription"));
        }

        return ResponseEntity.ok(Map.of("message", "Subscribed successfully"));
    }

    @DeleteMapping("/subscriptions/{subscriptionId}")
    public ResponseEntity<Void> unsubscribeFromSubject(HttpServletRequest request, @PathVariable Long subscriptionId) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        subscriptionService.unsubscribeFromSubject(subscriptionId);

        return ResponseEntity.noContent().build();
    }
}
