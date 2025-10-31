package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.*;
import com.openclassrooms.mddapi.security.services.AuthenticationService;
import com.openclassrooms.mddapi.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing posts and post comments.
 * Provides endpoints for retrieving posts, creating posts, and managing comments.
 * All endpoints require authentication handled by AuthByIdInterceptor.
 */
@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final AuthenticationService authenticationService;


    /**
     * Constructs a new PostController with the specified services.
     *
     * @param postService the service for post operations
     * @param authenticationService the service responsible for authentication management,
     *                              used to extract the user ID from the HTTP request with
     *                              {@code getUserIdFromHttpServletRequest()}
     */
    public PostController(PostService postService, AuthenticationService authenticationService) {
        this.postService = postService;
        this.authenticationService = authenticationService;
    }

    /**
     * Retrieves all posts from subscribed topics for the authenticated user.
     *
     * @param request the HTTP request containing user authentication information after JWT validation.
     * @return ResponseEntity containing a list of posts which the user authenticated is subscribed or 204 No Content if no posts found
     */
    @GetMapping("/posts/subscribed")
    public ResponseEntity<Iterable<PostListDto>> getSubscribedPostsForUser(HttpServletRequest request) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

        List<PostListDto> postsListDto = this.postService.getSubscribedPostsForUser(userId);

        if(postsListDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postsListDto);
    }

    /**
     * Finds a specific post by its ID.
     *
     * @param request the HTTP request containing user authentication information after JWT validation.
     * @param postId the ID of the post to find
     * @return ResponseEntity containing the post data or 404 Not Found if post doesn't exist
     */
    @GetMapping("posts/{postId}")
    public ResponseEntity<PostDto> findPostById(HttpServletRequest request, @PathVariable Long postId) {
        authenticationService.getUserIdFromHttpServletRequest(request);

        PostDto postDto = this.postService.findPostById(postId);
        if(postDto == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(postDto);
        }
    }

    /**
     * Return all comments for a specific post.
     *
     * @param request the HTTP request containing user authentication information after JWT validation.
     * @param postId the ID of the post to get comments for
     * @return ResponseEntity containing a list of comments or 204 No Content if no comments found
     */
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Iterable<CommentDto>> getCommentsForPost(HttpServletRequest request, @PathVariable Long postId) {
        authenticationService.getUserIdFromHttpServletRequest(request);
        Iterable<CommentDto> commentsDto = this.postService.findCommentsByPostId(postId);

        if(!commentsDto.iterator().hasNext()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(commentsDto);
    }

    /**
     * Creates a new post for the authenticated user.
     *
     * @param request the HTTP request containing user authentication information after JWT validation.
     * @param newPostDto the data for the new post to create
     * @return ResponseEntity containing success message or error details
     *         Returns 400 Bad Request if validation fails, 500 Internal Server Error for other errors
     */
    @PostMapping("/posts/create")
    public ResponseEntity<Map<String, String>> createPost(HttpServletRequest request, @RequestBody NewPostDto newPostDto) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

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

    /**
     * Adds a comment to a specific post.
     *
     * @param request the HTTP request containing user authentication information after JWT validation.
     * @param postId the ID of the post to comment on
     * @param message the comment message data
     * @return ResponseEntity containing success message or error details
     *         Returns 400 Bad Request if validation fails, 500 Internal Server Error for other errors
     */
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Map<String, String>> addCommentToPost(HttpServletRequest request, @PathVariable Long postId, @RequestBody NewMessageDto message) {
        Long userId = authenticationService.getUserIdFromHttpServletRequest(request);

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
}
