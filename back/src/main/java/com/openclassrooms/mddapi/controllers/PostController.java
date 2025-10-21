package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.*;
import com.openclassrooms.mddapi.exceptions.IllegalArgumentException;
import com.openclassrooms.mddapi.exceptions.UnauthorizedException;
import com.openclassrooms.mddapi.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/subscribed")
    public ResponseEntity<Iterable<PostListDto>> getSubscribedPostsForUser(HttpServletRequest request) {
        Long userId = userIsValid(request);

        List<PostListDto> postsListDto = this.postService.getSubscribedPostsForUser(userId);

        if(postsListDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postsListDto);
    }

    @GetMapping("posts/{postId}")
    public ResponseEntity<PostDto> findPostById(HttpServletRequest request, @PathVariable Long postId) {
        userIsValid(request);

        PostDto postDto = this.postService.findPostById(postId);
        if(postDto == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(postDto);
        }
    }

    @PostMapping("/posts/create")
    public ResponseEntity<Map<String, String>> createPost(HttpServletRequest request, @RequestBody NewPostDto newPostDto) {
        Long userId = userIsValid(request);

        try {
            this.postService.createPost(newPostDto, userId);
            return ResponseEntity.ok(Map.of("post", "created successfully"));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        catch(Exception ignored) {
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred while creating the post"));
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Iterable<CommentDto>> getCommentsForPost(HttpServletRequest request, @PathVariable Long postId) {
        userIsValid(request);
        Iterable<CommentDto> commentsDto = this.postService.findCommentsByPostId(postId);

        if(!commentsDto.iterator().hasNext()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(commentsDto);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Map<String, String>> addCommentToPost(HttpServletRequest request, @PathVariable Long postId, @RequestBody NewMessageDto message) {
        Long userId = userIsValid(request);

        try {
            this.postService.addCommentToPost(postId, userId, message.getMessage());
            return ResponseEntity.ok(Map.of("comment", "added successfully"));
        }
        catch(IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        catch(Exception ignored) {
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred while adding the comment"));
        }
    }

    private Long userIsValid(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if(userId == null) {
            throw new UnauthorizedException("User is not authenticated.");
        }
        return userId;
    }
}
