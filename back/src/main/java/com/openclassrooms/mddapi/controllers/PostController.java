package com.openclassrooms.mddapi.controllers;

import com.openclassrooms.mddapi.DTO.PostDto;
import com.openclassrooms.mddapi.DTO.PostListDto;
import com.openclassrooms.mddapi.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/subscribed")
    public ResponseEntity<Iterable<PostListDto>> getSubscribedPostsForUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if(userId == null) {
            return ResponseEntity.status(401).build();
        }
        List<PostListDto> postsListDto = this.postService.getSubscribedPostsForUser(userId);

        if(postsListDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postsListDto);
    }

}
