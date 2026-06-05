package com.realworld.backend.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Follow {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_username")
  private String from;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_username")
  private String to;

  public Follow(String from, String to) {
    this.from = from;
    this.to = to;
  }
}
