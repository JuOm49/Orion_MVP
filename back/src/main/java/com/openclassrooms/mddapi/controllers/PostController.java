package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.PostDto;
import com.openclassrooms.mddapi.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<Iterable<PostDto>> getPosts() {
        return ResponseEntity.ok(this.postService.getAllPosts());
    }
}
