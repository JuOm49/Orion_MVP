package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.SubscriptionDto;
import com.openclassrooms.mddapi.exceptions.IllegalArgumentException;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final SubjectService subjectService;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserService userService, SubjectService subjectService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    public Iterable<Subscription> findByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }


    @Transactional
    public List<SubscriptionDto> getAllSubscriptionsForUser(Long userId) {
        Iterable<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
       return convertSubscriptionToSubscriptionDto(subscriptions);
    }

    @Transactional
    public void subscribeToSubject(Long userId, Long subjectId) {
        User user = userService.getReferenceById(userId);
        Subject subject = subjectService.getReferenceById(subjectId);

        Subscription newSubscription = new Subscription();
        newSubscription.setUser(user);
        newSubscription.setSubject(subject);

        subscriptionRepository.save(newSubscription);
    }

    @Transactional
    public void unsubscribeFromSubject(Long userId, Long subjectId) {

        Subscription subscription = subscriptionRepository.findByUserIdAndSubjectId(userId, subjectId).orElse(null);
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription does not exist.");
        }

        subscriptionRepository.delete(subscription);
    }

    private List<SubscriptionDto> convertSubscriptionToSubscriptionDto(Iterable<Subscription> subscriptions) {
        List<SubscriptionDto> subscriptionsDto = new ArrayList<>();
        subscriptions.forEach(subscription -> {
            SubscriptionDto subscriptionDto = new SubscriptionDto();
            subscriptionDto.setId(subscription.getId());
            subscriptionDto.setUserId(subscription.getUser().getId());
            subscriptionDto.setSubjectId(subscription.getSubject().getId());
            subscriptionsDto.add(subscriptionDto);
        });
        return subscriptionsDto;
    }
}
