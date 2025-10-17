package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.*;
import com.openclassrooms.mddapi.models.Post;
import com.openclassrooms.mddapi.models.Subject;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.PostRepository;
import com.openclassrooms.mddapi.repositories.SubscriptionRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class PostService {

    private final PostRepository postRepository;
    private final SubscriptionRepository subscriptionRepository;

    public PostService(PostRepository postRepository, SubscriptionRepository subscriptionRepository) {
        this.postRepository = postRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public List<PostListDto> getSubscribedPostsForUser(Long userId) {
        Iterable<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        List<Long> subjectIds = new ArrayList<>();
        subscriptions.forEach(subscription -> {
           subjectIds.add(subscription.getSubject().getId());
        });

        Iterable<Post> posts = postRepository.findBySubjectIdIn(subjectIds);

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
        postListDto.setSubjectForPostListDtoList(setSubjectForPostListDto(post.getSubject()));

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

    private SubjectForPostListDto setSubjectForPostListDto(Subject subject) {
        SubjectForPostListDto subjectForPostListDto = new SubjectForPostListDto();
        subjectForPostListDto.setId(subject.getId());
        return subjectForPostListDto;
    }
}
