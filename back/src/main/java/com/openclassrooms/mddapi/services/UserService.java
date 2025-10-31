package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.RegisterUserDto;
import com.openclassrooms.mddapi.DTO.UserDto;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Data;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service that manages user operations such as creation, update, retrieval and DTO conversion.
 *
 * <p>This service delegates persistence operations to {@code UserRepository} and uses
 * {@code BCryptPasswordEncoder} to encode passwords. It can also provide a JPA reference
 * to a User via {@code EntityManager#getReference}.</p>
 */
@Data
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create a new instance of {@code UserService}.
     *
     * @param userRepository  repository used to persist and query users
     * @param passwordEncoder encoder used to hash user passwords
     */
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create and persist a new user from the provided registration DTO.
     *
     * <p>The method checks uniqueness by email and name. The provided password will be
     * encoded before persisting. If a user with the same email or name already exists,
     * an {@code IllegalArgumentException} is thrown.</p>
     *
     * @param registerUserDto DTO containing registration data
     * @return the persisted {@code User} entity
     * @throws IllegalArgumentException if a user with the same email or name already exists
     *                                  or if persistence fails due to integrity constraints
     */
    public User save(RegisterUserDto registerUserDto) {
        User newUser = registerUserDtoToUser(registerUserDto);
        Optional<User> userFindByEmail = findByEmail(newUser.getEmail());
        Optional<User> userFindByName = findByName(newUser.getName());

        if(userFindByEmail.isPresent() || userFindByName.isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        try {
            return userRepository.save(newUser);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("User already exists.", exception);
        }
    }

    /**
     * Update an existing user identified by {@code userId} using values from the DTO.
     *
     * <p>The method verifies that a user exists by email or name before updating. The
     * {@code updatedAt} timestamp is set to the current time and the password is encoded
     * prior to saving. If the user does not exist, an {@code IllegalArgumentException} is thrown.</p>
     *
     * @param userId          identifier of the user to update
     * @param registerUserDto DTO containing new values for the user
     * @return the updated and persisted {@code User} entity
     * @throws IllegalArgumentException if the user does not exist or if persistence fails due to integrity constraints
     */
    public User update(Long userId, RegisterUserDto registerUserDto) {
        User updateUser = registerUserDtoToUser(registerUserDto);
        Optional<User> userFindByEmail = findByEmail(updateUser.getEmail());
        Optional<User> userFindByName = findByName(updateUser.getName());

        if(userFindByEmail.isEmpty() && userFindByName.isEmpty()) {
            throw new IllegalArgumentException("User does not exist.");
        }

        updateUser.setId(userId);
        updateUser.setUpdatedAt(LocalDateTime.now());
        updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));

        try {
            return userRepository.save(updateUser);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("User update failed.", exception);
        }
    }

    /**
     * Find a user by its identifier.
     *
     * @param userId identifier of the user to retrieve
     * @return an {@code Optional} containing the {@code User} if found, otherwise {@code Optional.empty()}
     */
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Find a user by email. The provided email is normalized to lower case before querying.
     *
     * @param email email to search for
     * @return an {@code Optional} containing the {@code User} if found, otherwise {@code Optional.empty()}
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    /**
     * Find a user by name.
     *
     * @param name name to search for
     * @return an {@code Optional} containing the {@code User} if found, otherwise {@code Optional.empty()}
     */
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    /**
     * Obtain a JPA reference (proxy) for a {@code User} with the given identifier.
     *
     * <p>This method uses {@code EntityManager#getReference} to obtain a lazily-initialized
     * proxy without immediately loading the entity state. Accessing any property of the
     * returned proxy will trigger loading from the database and may throw
     * {@code jakarta.persistence.EntityNotFoundException} if the entity does not exist.</p>
     *
     * @param userId identifier of the user
     * @return a {@code User} instance which may be a proxy; accessing it may load the entity
     * @throws jakarta.persistence.EntityNotFoundException if the proxy is accessed and the entity is absent
     */
    public User getReferenceById(Long userId) {
        return entityManager.getReference(User.class, userId);
    }

    /**
     * Convert a {@code User} entity to a {@code UserDto} without exposing the password.
     *
     * @param user the {@code User} entity to convert
     * @return a {@code UserDto} containing public user fields
     */
    public UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());

        return userDto;
    }

    /**
     * Convert a {@code RegisterUserDto} into a new {@code User} entity instance.
     *
     * <p>The returned entity has its email normalized to lower case and timestamps for
     * {@code createdAt} and {@code updatedAt} initialized to the current time. The
     * password is not encoded by this helper.</p>
     *
     * @param registerUserDto the registration DTO to transform
     * @return a new {@code User} entity ready for persistence (password not yet encoded)
     */
    private User registerUserDtoToUser(RegisterUserDto registerUserDto) {
        User user = new User();
        user.setEmail(registerUserDto.getEmail().toLowerCase());
        user.setName(registerUserDto.getName());
        user.setPassword(registerUserDto.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}
