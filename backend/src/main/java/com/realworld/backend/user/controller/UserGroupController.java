package com.realworld.backend.user.controller;

import com.realworld.backend.user.dto.ResponseUser;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserGroupController {
  private final UserService userservice;

  @PostMapping("/")
  @RequestBody
  public ResponseUser register() {

  }

  @PostMapping("/login")
  @RequestBody
  public ResponseUser login() {

  }
}
