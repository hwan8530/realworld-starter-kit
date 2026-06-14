package com.realworld.backend.user.service;

import com.realworld.backend.common.errorhandling.handler.CustomException;
import com.realworld.backend.common.errorhandling.handler.CustomExceptionList;
import com.realworld.backend.mapper.UserMapper;
import com.realworld.backend.user.dto.ResponseProfile;
import com.realworld.backend.user.entity.Follow;
import com.realworld.backend.user.entity.User;
import com.realworld.backend.user.repository.FollowRepository;
import com.realworld.backend.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final UserMapper userMapper;

  /* 인증을 했다면 인증한 사람으로부터 username을 follow 했는지 검사 */
  public ResponseProfile getProfile(String username) {
    User searchUser = userRepository.findById(username)
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isfollowing = true;
    if (authentication == null || !authentication.isAuthenticated()) {
      isfollowing = false;
    }
    Follow follow = followRepository.findByFromUserNameAndToUserName(
        Objects.requireNonNull(authentication).getCredentials().toString(), username).orElse(null);
    if (follow == null) {
      isfollowing = false;
    }

    return new ResponseProfile(userMapper.userToProfile(searchUser, isfollowing));
  }

  public ResponseProfile followUser(String username) {
    String currentUsername = Objects.requireNonNull(
        SecurityContextHolder.getContext().getAuthentication()).getPrincipal().toString();
    User currentUser = userRepository.findByUsername(currentUsername)
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    User searchUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    Follow follow = followRepository.findByFromUserNameAndToUserName(currentUsername, username)
        .orElse(null);
    if (follow == null) {
      followRepository.save(new Follow(currentUser, searchUser));
    }
    return new ResponseProfile(userMapper.userToProfile(searchUser, true));
  }

  public ResponseProfile unfollowUser(String username) {
    String currentUsername = Objects.requireNonNull(
        SecurityContextHolder.getContext().getAuthentication()).getCredentials().toString();
    User searchUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    followRepository.findByFromUserNameAndToUserName(currentUsername, username).ifPresent(
        follow -> followRepository.deleteByFromUserNameAndToUserName(currentUsername, username));
    return new ResponseProfile(userMapper.userToProfile(searchUser, false));
  }
}
