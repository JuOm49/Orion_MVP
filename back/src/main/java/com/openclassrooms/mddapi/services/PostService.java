package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.*;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.User;
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

    public List<PostListDto> getAllPosts() {
        Iterable<Post> posts = postRepository.findAll();
        List<PostListDto> postsListDto = new ArrayList<>();
        for(Post post : posts) {
            postsListDto.add(convertPostToPostListDto(post));
        }
        return postsListDto;
    }

//    private PostDto convertPostToPostDto(Post post) {
//        SubjectDto subjectDto = new SubjectDto();
//        CommentDto commentDto = new CommentDto();
//        UserDto userDto = new UserDto();
//        PostDto postDto = new PostDto();
//
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setContent(post.getContent());
//        postDto.setCreatedAt(post.getCreatedAt());
//        postDto.setUpdatedAt(post.getUpdatedAt());
//
//        subjectDto.setId(post.getSubject().getId());
//        subjectDto.setTitle(post.getSubject().getTitle());
//        subjectDto.setDescription(post.getSubject().getDescription());
//        postDto.setSubjectDto(subjectDto);
//
//        userDto.setId(post.getUser().getId());
//        userDto.setName(post.getUser().getName());
//        postDto.setUserDto(userDto);
//
//        if(post.getComments() != null) {
//            List<CommentDto> commentsDto = new ArrayList<>();
//            post.getComments().forEach(comment -> {
//                commentDto.setId(comment.getId());
//                commentDto.setMessage(comment.getMessage());
//                commentDto.setCreatedAt(comment.getCreatedAt());
//                commentDto.setUpdatedAt(comment.getUpdatedAt());
//                commentsDto.add(commentDto);
//            });
//            postDto.setCommentsDto(commentsDto);
//        }
//
//        return postDto;
//    }

    private PostListDto convertPostToPostListDto(Post post) {
        PostListDto postListDto = new PostListDto();

        setPostListDtoFromPost(post, postListDto);
        postListDto.setUserForPostListDto(setUserDtoForPostsList(post.getUser()));

        return postListDto;
    }

    // set Methods for posts list
    private void setPostListDtoFromPost(Post post, PostListDto postListDto) {
        postListDto.setId(post.getId());
        postListDto.setTitle(post.getTitle());
        postListDto.setContent(post.getContent());
        postListDto.setUpdatedAt(post.getUpdatedAt());
    }

    private UserForPostListDto setUserDtoForPostsList(User user) {
        UserForPostListDto userForPostListDtoDto = new UserForPostListDto();
        userForPostListDtoDto.setId(user.getId());
        userForPostListDtoDto.setName(user.getName());
        return userForPostListDtoDto;
    }
}
