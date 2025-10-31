package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a post created by a user within a subject.
 *
 * <p>Holds the post title and content, automatic creation and update timestamps,
 * the authoring {@link User}, the associated {@link Subject}, and the list of
 * {@link Comment} entities attached to the post. Comments are cascaded and
 * removed when the post is deleted.</p>
 */
@Entity
@Data
@Table(name = "posts")
public class Post {
    /**
     * Primary key identifier of the post.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the post.
     *
     * <p>Required field with a maximum length of 300 characters.</p>
     */
    @Column(nullable = false, length = 300)
    private String title;

    /**
     * Main textual content of the post.
     *
     * <p>Required field with a maximum length of 15000 characters.</p>
     */
    @Column(nullable = false, length = 15000)
    private String content;

    /**
     * Timestamp when the post was created.
     *
     * <p>Automatically populated on insert and not updatable.</p>
     */
    @CreationTimestamp
    @Column(name= "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the post was last updated.
     *
     * <p>Automatically updated on entity modification.</p>
     */
    @UpdateTimestamp
    @Column(name= "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Author of the post.
     *
     * <p>Many-to-one association to {@link User} using lazy fetching. The database
     * column is {@code user_id} and is required. Accessing this proxy may trigger a
     * database load.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name= "fk_post_user"))
    private User user;

    /**
     * Subject/category this post belongs to.
     *
     * <p>Many-to-one association to {@link Subject} using lazy fetching. The database
     * column is {@code subject_id} and is required. Accessing this proxy may trigger a
     * database load.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false, foreignKey = @ForeignKey(name= "fk_post_subject"))
    private Subject subject;

    /**
     * Comments attached to the post.
     *
     * <p>One-to-many association mapped by {@code Comment.post}. CascadeType.ALL and
     * orphanRemoval ensure child comments are persisted and removed together with the post.
     * Initialized to an empty list to avoid null checks.</p>
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
