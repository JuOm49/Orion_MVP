package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.PostDto;
import com.openclassrooms.mddapi.DTO.PostListDto;
import com.openclassrooms.mddapi.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public ResponseEntity<Iterable<PostListDto>> getPosts() {
        List<PostListDto> postsListDto = this.postService.getAllPosts();

        if(postsListDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postsListDto);
    }
}
