package com.realworld.backend.article.repository;

import com.realworld.backend.article.entity.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

  Optional<Tag> findByTagName(String tagName);

  boolean existsByTagName(String tagName);
}
