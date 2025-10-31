package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a user's subscription to a subject.
 *
 * <p>Links a {@link User} and a {@link Subject} to represent that the user
 * is subscribed to the subject. Associations use lazy fetching and explicit
 * foreign key column names are provided. The {@code toString} output excludes
 * the relationships to avoid circular references and large outputs.</p>
 */
@Entity
@Data
@Table(name = "subscriptions")
public class Subscription {
    /**
     * Primary key identifier of the subscription.
     */
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    /**
     * The user who subscribed.
     *
     * <p>Many-to-one association to {@link User} using lazy fetching. The
     * database column is {@code user_id} and is required. Excluded from
     * {@code toString()} to avoid recursive printing.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name= "fk_subscription_user"))
    @ToString.Exclude
    private User user;

    /**
     * The subject to which the user is subscribed.
     *
     * <p>Many-to-one association to {@link Subject} using lazy fetching. The
     * database column is {@code subject_id} and is required. Excluded from
     * {@code toString()} to avoid recursive printing.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false, foreignKey = @ForeignKey(name= "fk_subscription_subject"))
    @ToString.Exclude
    private Subject subject;
}
