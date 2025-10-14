package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.SubscriptionDto;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
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

    public void unsubscribeFromSubject(Long subscriptionId) {
        subscriptionRepository.findById(subscriptionId).ifPresent(subscriptionRepository::delete);
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
