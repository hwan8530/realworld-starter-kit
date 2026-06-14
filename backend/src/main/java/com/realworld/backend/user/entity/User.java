package com.realworld.backend.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;
  @NotNull
  private String username;
  @NotNull
  private String password;
  @Column(unique = true)
  private String email;
  private String bio;
  private String image;
  @Enumerated(EnumType.STRING)
  private UserRole role;

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.bio = null;
    this.image = null;
    this.role = UserRole.USER;
  }
}
