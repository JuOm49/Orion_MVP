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
 * Entity representing an application user.
 *
 * <p>Holds basic user credentials and relationships to posts, comments and
 * subject subscriptions. The {@code toString} output excludes collections
 * and the password to avoid sensitive or large/circular outputs.</p>
 */
@Entity
@Data
@ToString(exclude = {"posts", "subscriptions", "comments", "password"})
@Table(name = "users")
public class User {
    /**
     * Primary key identifier of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username chosen by the user.
     *
     * <p>Required, unique and limited to 80 characters.</p>
     */
    @Column(nullable = false, length = 80, unique = true)
    private String name;

    /**
     * Email address of the user.
     *
     * <p>Required and must be unique. No length constraint configured here.</p>
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Hashed password for authentication.
     *
     * <p>Stored as a non-null field. Excluded from {@code toString()} for security.</p>
     */
    @Column(nullable = false)
    private String password;

    /**
     * Timestamp when the user was created.
     *
     * <p>Automatically populated on insert and not updatable.</p>
     */
    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user was last updated.
     *
     * <p>Automatically updated on entity modification.</p>
     */
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    /**
     * Comments authored by the user.
     *
     * <p>One-to-many association mapped by {@link Comment#user}. Cascade types
     * PERSIST and MERGE allow comments to be persisted/merged with the user.
     * Initialized to an empty list to avoid null checks.</p>
     */
    @OneToMany(mappedBy= "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> comments = new ArrayList<>();

    /**
     * Subscriptions owned by the user.
     *
     * <p>One-to-many association mapped by {@link Subscription#user}. Cascade types
     * PERSIST and MERGE allow subscriptions to be persisted/merged with the user.
     * Initialized to an empty list to avoid null checks.</p>
     */
    @OneToMany(mappedBy= "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subscription> subscriptions = new ArrayList<>();

    /**
     * Posts authored by the user.
     *
     * <p>One-to-many association mapped by {@link Post#user}. Cascade types
     * PERSIST and MERGE allow posts to be persisted/merged with the user.
     * Initialized to an empty list to avoid null checks.</p>
     */
    @OneToMany(mappedBy= "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Post> posts = new ArrayList<>();
}
