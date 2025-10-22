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
import com.openclassrooms.mddapi.exceptions.IllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    public User getReferenceById(Long userId) {
        return entityManager.getReference(User.class, userId);
    }

    public UserDto convertUserToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());

        return userDto;
    }


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
