package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsible for comment operations such as retrieval and persistence.
 *
 * <p>Delegates persistence to {@code CommentRepository} and provides methods to
 * find comments by post id and to save comment entities.</p>
 */
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    /**
     * Construct a new {@code CommentService}.
     *
     * @param commentRepository repository used to persist and query {@code Comment} entities
     */
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     * Find comments that belong to the specified post.
     *
     * @param postId identifier of the post whose comments should be retrieved
     * @return an {@code Iterable} of {@code Comment} entities for the post
     */
    public Iterable<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    /**
     * Persist the given comment entity.
     *
     * @param comment the {@code Comment} to save
     */
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
