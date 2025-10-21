package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.models.Subscription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    Iterable<Subscription> findByUserId(Long userId);
    Optional<Subscription> findByUserIdAndSubjectId(Long userId, Long subjectId);
}
