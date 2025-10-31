package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a subject or category that groups posts and user subscriptions.
 *
 * <p>Contains the subject title and description, automatic creation and update timestamps,
 * and associations to {@link Post} and {@link Subscription} entities. The {@code toString}
 * representation excludes collections to avoid large or circular outputs.</p>
 */
@Entity
@Data
@ToString(exclude = {"posts", "subscriptions"})
@Table(name = "subjects")
public class Subject {
    /**
     * Primary key identifier of the subject.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the subject.
     *
     * <p>Required field with a maximum length of 300 characters.</p>
     */
    @Column(nullable = false, length = 300)
    private String title;

    /**
     * Description of the subject.
     *
     * <p>Required field with a maximum length of 3000 characters.</p>
     */
    @Column(nullable = false, length = 3000)
    private String description;

    /**
     * Timestamp when the subject was created.
     *
     * <p>Automatically populated on insert and not updatable.</p>
     */
    @CreationTimestamp
    @Column(name= "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the subject was last updated.
     *
     * <p>Automatically updated on entity modification.</p>
     */
    @UpdateTimestamp
    @Column(name= "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Posts that belong to this subject.
     *
     * <p>One-to-many association mapped by {@code Post.subject}. Cascade types Persist and Merge
     * allow child posts to be persisted or merged when the subject is persisted/merged.
     * Initialized to an empty list to avoid null checks.</p>
     */
    @OneToMany(mappedBy = "subject", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Post> posts = new ArrayList<>();

    /**
     * Subscriptions to this subject.
     *
     * <p>One-to-many association mapped by {@code Subscription.subject}. Cascade types Persist and Merge
     * allow subscriptions to be persisted/merged together with the subject.
     * Initialized to an empty list to avoid null checks.</p>
     */
    @OneToMany(mappedBy = "subject", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subscription> subscriptions = new ArrayList<>();
}
