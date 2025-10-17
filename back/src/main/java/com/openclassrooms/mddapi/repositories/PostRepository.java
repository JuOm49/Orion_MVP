package com.openclassrooms.mddapi.repositories;

import com.openclassrooms.mddapi.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
   Iterable<Post> findBySubjectIdIn(Collection<Long> ids);
}
