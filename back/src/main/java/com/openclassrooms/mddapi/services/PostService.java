package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.*;
import com.openclassrooms.mddapi.exceptions.IllegalArgumentException;
import com.openclassrooms.mddapi.exceptions.NotFoundException;
import com.openclassrooms.mddapi.models.*;
import com.openclassrooms.mddapi.repositories.PostRepository;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class PostService {

    private final PostRepository postRepository;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final SubjectService subjectService;

    public PostService(PostRepository postRepository, SubscriptionService subscriptionService , UserService userService, SubjectService subjectService) {
        this.postRepository = postRepository;
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    public List<PostListDto> getSubscribedPostsForUser(Long userId) {
        Iterable<Subscription> subscriptions = subscriptionService.findByUserId(userId);
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

    public PostDto findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post not found"));

        return convertPostToPostDto(post);
    }

    @Transactional
    public void createPost(NewPostDto newPostDto, Long userId) {
        if (newPostDto == null
                || !StringUtils.hasText(newPostDto.getTitle())
                || !StringUtils.hasText(newPostDto.getContent())) {
            throw new IllegalArgumentException("title and content are required to create a post.");
        }

        User user = this.userService.findById(userId)
               .orElseThrow(()-> new NotFoundException("User not found"));

       Subject subject = this.subjectService.findById(newPostDto.getSubjectId())
               .orElseThrow(()-> new NotFoundException("Subject not found"));

       Post post = convertNewPostDtoToPost(newPostDto, user, subject);

        postRepository.save(post);
    }

    private Post convertNewPostDtoToPost(NewPostDto newPostDto, User user, Subject subject) {
        Post post = new Post();
        post.setTitle(newPostDto.getTitle());
        post.setContent(newPostDto.getContent());
        post.setSubject(subject);
        post.setUser(user);

        return post;
    }

    private PostDto convertPostToPostDto(Post post) {
        SubjectDto subjectDto = new SubjectDto();
        CommentDto commentDto = new CommentDto();
        UserDto userDto = new UserDto();
        PostDto postDto = new PostDto();

        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setUpdatedAt(post.getUpdatedAt());

        subjectDto.setId(post.getSubject().getId());
        subjectDto.setTitle(post.getSubject().getTitle());
        subjectDto.setDescription(post.getSubject().getDescription());
        postDto.setSubjectDto(subjectDto);

        userDto.setId(post.getUser().getId());
        userDto.setName(post.getUser().getName());
        postDto.setUserDto(userDto);

        if(post.getComments() != null) {
            List<CommentDto> commentsDto = new ArrayList<>();
            post.getComments().forEach(comment -> {
                commentDto.setId(comment.getId());
                commentDto.setMessage(comment.getMessage());
                commentDto.setCreatedAt(comment.getCreatedAt());
                commentDto.setUpdatedAt(comment.getUpdatedAt());
                commentDto.setUserDto(setCommentUserToUserDtoForPostDetail(comment));
                commentsDto.add(commentDto);
            });
            postDto.setCommentsDto(commentsDto);
        }

        return postDto;
    }

    private PostListDto convertPostToPostListDto(Post post) {
        PostListDto postListDto = new PostListDto();

        setPostListDtoFromPost(post, postListDto);
        postListDto.setUserForPostListDto(setUserDtoForPostsList(post.getUser()));
        postListDto.setSubjectForPostListDtoList(setSubjectForPostListDto(post.getSubject()));

        return postListDto;
    }

    //set Method for post detail
    private UserDto setCommentUserToUserDtoForPostDetail(Comment comment) {
        UserDto userDto = new UserDto();
        userDto.setId(comment.getUser().getId());
        userDto.setName(comment.getUser().getName());
        return userDto;
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
