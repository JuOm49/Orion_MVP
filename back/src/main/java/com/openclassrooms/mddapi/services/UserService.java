package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.RegisterUserDto;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

        return userRepository.save(newUser);
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

        return userRepository.save(updateUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
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
