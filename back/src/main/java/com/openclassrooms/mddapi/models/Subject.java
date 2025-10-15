package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString(exclude = {"posts", "subscriptions"})
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(nullable = false, length = 3000)
    private String description;

    @OneToMany(mappedBy = "subject", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Subscription> subscriptions = new ArrayList<>();
}
