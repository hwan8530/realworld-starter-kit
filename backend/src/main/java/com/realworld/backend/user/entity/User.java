package com.realworld.backend.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {
  @Id
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
}
