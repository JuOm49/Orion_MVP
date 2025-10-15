package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy= "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy= "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy= "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Post> posts = new ArrayList<>();
}
