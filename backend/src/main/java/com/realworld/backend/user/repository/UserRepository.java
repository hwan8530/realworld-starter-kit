package com.realworld.backend.user.repository;

import com.realworld.backend.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
  Boolean existsByEmail(String email);
  Optional<User> findByEmail(String email);
}
