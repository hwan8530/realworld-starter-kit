package com.realworld.backend.user.controller;

import com.realworld.backend.user.dto.RequestUser;
import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.dto.RequestUser.Register;
import com.realworld.backend.user.dto.ResponseUser;
import com.realworld.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserGroupController {

  private final UserService userservice;

  @PostMapping("/")
  public ResponseEntity<ResponseUser> register(@RequestBody RequestUser<Register> requestUser) {
    return new ResponseEntity<ResponseUser>(userservice.registration(requestUser.getUser()),
        HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<ResponseUser> login(@RequestBody RequestUser<Login> requestUser) {
    return new ResponseEntity<ResponseUser>(userservice.login(requestUser.getUser()),
        HttpStatus.OK);
  }
}
