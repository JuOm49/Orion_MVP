package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.SubscriptionDto;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for managing user subscriptions to subjects.
 *
 * <p>This service provides methods to query subscriptions for a user, subscribe and unsubscribe
 * from subjects, and convert subscription entities to DTOs. It relies on {@code SubscriptionRepository}
 * for persistence and uses {@code UserService} and {@code SubjectService} to obtain entity references.</p>
 */
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserService userService;
    private final SubjectService subjectService;

    /**
     * Construct a new {@code SubscriptionService}.
     *
     * @param subscriptionRepository repository used to persist and query subscriptions
     * @param userService service used to obtain user references
     * @param subjectService service used to obtain subject references
     */
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserService userService, SubjectService subjectService) {
        this.subscriptionRepository = subscriptionRepository;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    /**
     * Find all subscriptions that belong to the given user id.
     *
     * @param userId identifier of the user whose subscriptions should be retrieved
     * @return an {@code Iterable} of {@code Subscription} entities for the user
     */
    public Iterable<Subscription> findByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    /**
     * Get all subscriptions for a user and convert them to DTOs.
     *
     * <p>This method is transactional and will fetch subscriptions via the repository,
     * then transform them into {@code SubscriptionDto} instances.</p>
     *
     * @param userId identifier of the user
     * @return a {@code List} of {@code SubscriptionDto} representing the user's subscriptions
     */
    @Transactional
    public List<SubscriptionDto> getAllSubscriptionsForUser(Long userId) {
        Iterable<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
       return convertSubscriptionToSubscriptionDto(subscriptions);
    }

    /**
     * Create a subscription linking the given user and subject.
     *
     * <p>This method obtains JPA references for user and subject via their services and
     * persists a new {@code Subscription} entity.</p>
     *
     * @param userId identifier of the user who subscribes
     * @param subjectId identifier of the subject to subscribe to
     * @throws jakarta.persistence.EntityNotFoundException if the user or subject reference does not exist when accessed
     */
    @Transactional
    public void subscribeToSubject(Long userId, Long subjectId) {
        User user = userService.getReferenceById(userId);
        Subject subject = subjectService.getReferenceById(subjectId);

        Subscription newSubscription = new Subscription();
        newSubscription.setUser(user);
        newSubscription.setSubject(subject);

        subscriptionRepository.save(newSubscription);
    }

    /**
     * Remove an existing subscription between a user and a subject.
     *
     * <p>If no subscription exists for the given user and subject, an {@code IllegalArgumentException}
     * is thrown.</p>
     *
     * @param userId identifier of the user
     * @param subjectId identifier of the subject
     * @throws IllegalArgumentException if the subscription does not exist
     */
    @Transactional
    public void unsubscribeFromSubject(Long userId, Long subjectId) {

        Subscription subscription = subscriptionRepository.findByUserIdAndSubjectId(userId, subjectId).orElse(null);
        if (subscription == null) {
            throw new IllegalArgumentException("Subscription does not exist.");
        }

        subscriptionRepository.delete(subscription);
    }

    /**
     * Convert an iterable of {@code Subscription} entities into a list of {@code SubscriptionDto}.
     *
     * @param subscriptions iterable collection of subscription entities
     * @return a {@code List} of {@code SubscriptionDto} with id, userId and subjectId populated
     */
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
