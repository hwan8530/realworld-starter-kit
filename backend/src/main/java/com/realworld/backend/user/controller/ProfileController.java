package com.realworld.backend.user.controller;

import com.realworld.backend.user.dto.ResponseProfile;
import com.realworld.backend.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
  private final ProfileService profileService;

  @GetMapping("/{username}")
  public ResponseEntity<ResponseProfile> getProfile(@PathVariable String username) {
    return new ResponseEntity<>(profileService.getProfile(username), HttpStatus.OK);
  }

  @PostMapping("/{username}/follow")
  public ResponseEntity<ResponseProfile> followProfile(@PathVariable String username) {
    return new ResponseEntity<>(profileService.followUser(username), HttpStatus.OK);
  }

  @DeleteMapping("/{username}/follow")
  public ResponseEntity<ResponseProfile> unfollowProfile(@PathVariable String username) {
    return new ResponseEntity<>(profileService.unfollowUser(username), HttpStatus.NO_CONTENT);
  }
}
