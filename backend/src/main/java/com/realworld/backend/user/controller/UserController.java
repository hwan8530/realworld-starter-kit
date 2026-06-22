package com.realworld.backend.user.controller;

import com.realworld.backend.user.dto.RequestUser;
import com.realworld.backend.user.dto.RequestUser.UpdateRequest;
import com.realworld.backend.user.dto.ResponseUser;
import com.realworld.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("")
  public ResponseEntity<ResponseUser> getCurrentUser() {
    return new ResponseEntity<>(userService.getUser(), HttpStatus.OK);
  }

  @PutMapping("")
  public ResponseEntity<ResponseUser> updateUser(
      @RequestBody RequestUser<UpdateRequest> updateRequest) {
    return new ResponseEntity<>(userService.updateUser(updateRequest.getUser()), HttpStatus.OK);
  }
}
