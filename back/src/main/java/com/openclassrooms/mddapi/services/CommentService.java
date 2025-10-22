package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.repositories.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Iterable<Comment> findByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }
}
