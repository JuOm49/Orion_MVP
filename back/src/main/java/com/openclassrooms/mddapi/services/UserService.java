package com.openclassrooms.mddapi.services;

import com.openclassrooms.mddapi.DTO.RegisterUserDto;
import com.openclassrooms.mddapi.DTO.UserDto;
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

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public User save(RegisterUserDto registerUserDto)
    {
        User newUser = registerUserDtoToUser(registerUserDto);
        Optional<User> userFindByEmail = userRepository.findByEmail(newUser.getEmail());
        Optional<User> userFindByName = userRepository.findByName(newUser.getName());

        if(userFindByEmail.isPresent() || userFindByName.isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        return userRepository.save(newUser);
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
