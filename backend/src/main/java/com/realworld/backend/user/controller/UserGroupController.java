package com.realworld.backend.user.controller;

import com.realworld.backend.user.dto.RequestUser;
import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.dto.RequestUser.Register;
import com.realworld.backend.user.dto.ResponseUser;
import com.realworld.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserGroupController {
  private final UserService userservice;

  @PostMapping("/")
  public ResponseUser register(@RequestBody RequestUser<Register> requestUser) {
    return userservice.registration(requestUser.getUser());
  }

  @PostMapping("/login")
  public ResponseUser login(@RequestBody RequestUser<Login> requestUser) {
    return userservice.login(requestUser.getUser());
  }
}
