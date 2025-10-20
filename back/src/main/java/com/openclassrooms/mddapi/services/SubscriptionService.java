package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.SubscriptionDto;
import com.openclassrooms.mddapi.exceptions.IllegalArgumentException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Iterable<Subscription> findByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }


    @Transactional
    public List<SubscriptionDto> getAllSubscriptionsForUser(Long userId) {
        Iterable<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
       return convertSubscriptionToSubscriptionDto(subscriptions);
    }

    public SubscriptionDto subscribeToSubject(Subscription newSubscription) {
        Subscription savedSubscription = subscriptionRepository.save(newSubscription);
        SubscriptionDto subscriptionDto = new SubscriptionDto();
        subscriptionDto.setId(savedSubscription.getId());
        return subscriptionDto;
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
