package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.PostDto;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.repositories.PostRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Iterable<PostDto> getAllPosts() {
        Iterable<Post> posts = postRepository.findAll();
        List<PostDto> postsDto = new ArrayList<>();
        for(Post post : posts) {
            postsDto.add(postToPostDto(post));
        }
        return postsDto;
    }


    private PostDto postToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setUpdatedAt(post.getUpdatedAt());
        return postDto;
    }
}
