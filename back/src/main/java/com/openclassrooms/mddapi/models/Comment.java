package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a comment made by a user on a post.
 *
 * <p>Stores the comment text, creation and update timestamps, and references to the
 * authoring {@code User} and the associated {@code Post}. The {@code toString}
 * representation excludes user and post to avoid circular references.</p>
 */
@Entity
@Data
@ToString(exclude= {"user", "post"})
@Table(name = "comments")
public class Comment {
    /**
     * Primary key identifier of the comment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Text content of the comment.
     *
     * <p>Maximum length is 2000 characters and the field is required (non-null).</p>
     */
    @Column(nullable = false, length = 2000)
    private String message;

    /**
     * Timestamp when the comment was created.
     *
     * <p>Automatically populated on insert and not updatable.</p>
     */
    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the comment was last updated.
     *
     * <p>Automatically updated on entity modification.</p>
     */
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    /**
     * Author of the comment.
     *
     * <p>Many-to-one association to {@link User} using lazy fetching. The database column is
     * {@code user_id} and is required. Accessing this proxy may trigger a database load.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name= "fk_comment_user"))
    private User user;

    /**
     * Post to which this comment belongs.
     *
     * <p>Many-to-one association to {@link Post} using lazy fetching. The database column is
     * {@code post_id} and is required. Accessing this proxy may trigger a database load.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name= "fk_comment_post"))
    private Post post;
}
