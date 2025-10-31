package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.*;
import com.openclassrooms.mddapi.models.*;
import com.openclassrooms.mddapi.repositories.PostRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service responsible for managing posts, including creation, retrieval and conversion
 * between entity models and DTOs used by the application.
 *
 * <p>This service uses {@code PostRepository} for persistence and collaborates with
 * {@code SubscriptionService}, {@code UserService}, {@code SubjectService} and
 * {@code CommentService} to obtain related entities and perform operations.</p>
 */
@Service
public class PostService {

    private final PostRepository postRepository;
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private final SubjectService subjectService;
    private final CommentService commentService;

    /**
     * Construct a new {@code PostService}.
     *
     * @param postRepository repository used to persist and query {@code Post} entities
     * @param subscriptionService service used to query user subscriptions
     * @param userService service used to retrieve user entities
     * @param subjectService service used to retrieve subject entities
     * @param commentService service used to manage comments
     */
    public PostService(PostRepository postRepository, SubscriptionService subscriptionService , UserService userService, SubjectService subjectService, CommentService commentService) {
        this.postRepository = postRepository;
        this.subscriptionService = subscriptionService;
        this.userService = userService;
        this.subjectService = subjectService;
        this.commentService = commentService;
    }

    /**
     * Retrieve posts for subjects to which the specified user is subscribed.
     *
     * <p>The method collects the subject ids from the user's subscriptions and uses the
     * repository to find posts belonging to those subjects, then converts each post to
     * a {@code PostListDto}.</p>
     *
     * @param userId identifier of the user
     * @return a {@code List} of {@code PostListDto} for the user's subscribed subjects
     */
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

    /**
     * Find a post by its identifier and convert it to {@code PostDto}.
     *
     * @param postId identifier of the post
     * @return a {@code PostDto} representing the post
     * @throws NoSuchElementException if the post does not exist
     */
    public PostDto findPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        return convertPostToPostDto(post);
    }

    /**
     * Retrieve comments for a specific post and convert them to DTOs.
     *
     * @param postId identifier of the post
     * @return an {@code Iterable} of {@code CommentDto} for the post
     */
    public Iterable<CommentDto> findCommentsByPostId(Long postId) {
        Iterable<Comment> comments = commentService.findByPostId(postId);
        return convertCommentsToCommentDtos(comments);
    }

    /**
     * Create a new post authored by the specified user.
     *
     * <p>The method validates input fields, verifies that the user and subject exist,
     * converts the DTO to an entity and persists it.</p>
     *
     * @param newPostDto DTO containing new post data
     * @param userId identifier of the authoring user
     * @throws IllegalArgumentException if required fields are missing
     * @throws NoSuchElementException if the user or subject cannot be found
     */
    @Transactional
    public void createPost(NewPostDto newPostDto, Long userId) {
        if (newPostDto == null
                || !StringUtils.hasText(newPostDto.getTitle())
                || !StringUtils.hasText(newPostDto.getContent())) {
            throw new IllegalArgumentException("title and content are required to create a post.");
        }

        User user = this.userService.findById(userId)
               .orElseThrow(()-> new NoSuchElementException("User not found"));

       Subject subject = this.subjectService.findById(newPostDto.getSubjectId())
               .orElseThrow(()-> new NoSuchElementException("Subject not found"));

       Post post = convertNewPostDtoToPost(newPostDto, user, subject);

        postRepository.save(post);
    }

    /**
     * Add a new comment to an existing post.
     *
     * <p>The method validates the message, ensures the user and post exist, creates
     * a new {@code Comment} entity and persists it via {@code CommentService}.</p>
     *
     * @param postId identifier of the post to comment on
     * @param userId identifier of the commenting user
     * @param message text of the comment
     * @throws IllegalArgumentException if the message is empty
     * @throws NoSuchElementException if the user or post cannot be found
     */
    @Transactional
    public void addCommentToPost(Long postId, Long userId, String message) {
        if (!StringUtils.hasText(message)) {
            throw new IllegalArgumentException("Comment message cannot be empty.");
        }

        User user = userService.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));

        Comment comment = new Comment();
        comment.setMessage(message);
        comment.setPost(post);
        comment.setUser(user);

        post.getComments().add(comment);

        commentService.saveComment(comment);
    }

    /**
     * Convert a {@code NewPostDto} to a {@code Post} entity using provided user and subject.
     *
     * @param newPostDto DTO containing post fields
     * @param user authoring user entity
     * @param subject subject entity associated with the post
     * @return a new {@code Post} entity ready for persistence
     */
    private Post convertNewPostDtoToPost(NewPostDto newPostDto, User user, Subject subject) {
        Post post = new Post();
        post.setTitle(newPostDto.getTitle());
        post.setContent(newPostDto.getContent());
        post.setSubject(subject);
        post.setUser(user);

        return post;
    }

    /**
     * Convert a {@code Post} entity to a detailed {@code PostDto}, including subject,
     * user summary and comments.
     *
     * @param post the {@code Post} entity to convert
     * @return a {@code PostDto} with populated fields and nested DTOs
     * @throws IllegalArgumentException if the post or required associations are null
     */
    private PostDto convertPostToPostDto(Post post) {
        if(post == null) {
            throw new IllegalArgumentException("Post cannot be null.");
        }

        if(post.getUser() == null) {
            throw new IllegalArgumentException("Post's user cannot be null.");
        }

        if(post.getSubject() == null) {
            throw new IllegalArgumentException("Post's subject cannot be null.");
        }

        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setCreatedAt(post.getCreatedAt());
        postDto.setUpdatedAt(post.getUpdatedAt());

        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(post.getSubject().getId());
        subjectDto.setTitle(post.getSubject().getTitle());
        subjectDto.setDescription(post.getSubject().getDescription());
        postDto.setSubjectDto(subjectDto);

        UserDto userDto = new UserDto();
        userDto.setId(post.getUser().getId());
        userDto.setName(post.getUser().getName());
        postDto.setUserDto(userDto);

        Iterable<CommentDto> allComments = findCommentsByPostId(post.getId());
        List<CommentDto> commentsDto = new ArrayList<>();
        if(allComments != null) {
            allComments.forEach(commentsDto::add);
        }

        postDto.setCommentsDto(commentsDto);

        return postDto;
    }

    /**
     * Convert a {@code Post} to a compact {@code PostListDto} used for listing pages.
     *
     * @param post the {@code Post} entity to convert
     * @return a {@code PostListDto} with summary fields, user and subject info
     */
    private PostListDto convertPostToPostListDto(Post post) {
        PostListDto postListDto = new PostListDto();

        setPostListDtoFromPost(post, postListDto);
        postListDto.setUserForPostListDto(setUserDtoForPostsList(post.getUser()));
        postListDto.setSubjectForPostListDtoList(setSubjectForPostListDto(post.getSubject()));

        return postListDto;
    }

    //set Method for post detail
    /**
     * Map the comment's user to a {@code UserDto} used inside post detail responses.
     *
     * @param comment the {@code Comment} whose user will be converted
     * @return a {@code UserDto} with the comment author summary
     */
    private UserDto setCommentUserToUserDtoForPostDetail(Comment comment) {
        UserDto userDto = new UserDto();
        userDto.setId(comment.getUser().getId());
        userDto.setName(comment.getUser().getName());
        return userDto;
    }

    /**
     * Convert an iterable of {@code Comment} entities to {@code CommentDto} instances.
     *
     * @param comments iterable collection of comment entities
     * @return an {@code Iterable} of {@code CommentDto} with user info included
     */
    private Iterable<CommentDto> convertCommentsToCommentDtos(Iterable<Comment> comments) {
        List<CommentDto> commentDtos = new ArrayList<>();
        comments.forEach(comment -> {
            CommentDto commentDto = new CommentDto();
            commentDto.setId(comment.getId());
            commentDto.setMessage(comment.getMessage());
            commentDto.setCreatedAt(comment.getCreatedAt());
            commentDto.setUpdatedAt(comment.getUpdatedAt());

            UserDto userDto = new UserDto();
            userDto.setId(comment.getUser().getId());
            userDto.setName(comment.getUser().getName());
            commentDto.setUserDto(userDto);

            commentDtos.add(commentDto);
        });
        return commentDtos;
    }

    // set Methods for posts list
    /**
     * Populate basic fields of {@code PostListDto} from a {@code Post} entity.
     *
     * @param post source {@code Post} entity
     * @param postListDto target {@code PostListDto} to populate
     */
    private void setPostListDtoFromPost(Post post, PostListDto postListDto) {
        postListDto.setId(post.getId());
        postListDto.setTitle(post.getTitle());
        postListDto.setContent(post.getContent());
        postListDto.setUpdatedAt(post.getUpdatedAt());
    }

    /**
     * Create a {@code UserForPostListDto} from a {@code User} entity for listing contexts.
     *
     * @param user source {@code User} entity
     * @return a {@code UserForPostListDto} with id and name populated
     */
    private UserForPostListDto setUserDtoForPostsList(User user) {
        UserForPostListDto userForPostListDtoDto = new UserForPostListDto();
        userForPostListDtoDto.setId(user.getId());
        userForPostListDtoDto.setName(user.getName());
        return userForPostListDtoDto;
    }

    /**
     * Create a {@code SubjectForPostListDto} from a {@code Subject} entity for listing contexts.
     *
     * @param subject source {@code Subject} entity
     * @return a {@code SubjectForPostListDto} with id populated
     */
    private SubjectForPostListDto setSubjectForPostListDto(Subject subject) {
        SubjectForPostListDto subjectForPostListDto = new SubjectForPostListDto();
        subjectForPostListDto.setId(subject.getId());
        return subjectForPostListDto;
    }
}
