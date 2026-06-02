package com.realworld.backend.user.controller;

import com.realworld.backend.user.dto.ResponseUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final UserSerivce userSerivce;

  @GetMapping("/")
  public ResponseEntity<ResponseUser> getCurrentUser() {

  }

  @PutMapping("/")
  @RequestBody
  public ResponseEntity<ResponseUser> updateUser() {

  }
}
