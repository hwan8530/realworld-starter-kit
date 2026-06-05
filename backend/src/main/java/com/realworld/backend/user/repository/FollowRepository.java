package com.realworld.backend.user.repository;

import com.realworld.backend.user.entity.Follow;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  Optional<Follow> findByFromUserNameAndToUserName(String from, String to);
  void deleteByFromUserNameAndToUserName(String from, String to);
}
