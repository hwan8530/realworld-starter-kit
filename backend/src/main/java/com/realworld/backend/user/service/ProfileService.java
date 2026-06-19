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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProfileService {

  private final UserRepository userRepository;
  private final FollowRepository followRepository;
  private final UserMapper userMapper;

  /* 인증을 했다면 인증한 사람으로부터 username을 follow 했는지 검사 */
  public ResponseProfile getProfile(String username) {
    User searchUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isfollowing = true;
    if (authentication == null || !authentication.isAuthenticated()) {
      isfollowing = false;
    }
    log.debug("searchUser : {}", searchUser.getUsername());
    log.debug("userName from token : {}", Objects.requireNonNull(
        Objects.requireNonNull(authentication).getPrincipal()).toString());
    Follow follow = followRepository.findByFromUserNameAndToUserName(
        Objects.requireNonNull(authentication).getPrincipal().toString(), username).orElse(null);
    if (follow == null) {
      isfollowing = false;
    }

    return new ResponseProfile(userMapper.userToProfile(searchUser, isfollowing));
  }

  @Transactional
  public ResponseProfile followUser(String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }
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

  @Transactional
  public ResponseProfile unfollowUser(String username) {
    String currentUsername = Objects.requireNonNull(
        SecurityContextHolder.getContext().getAuthentication()).getPrincipal().toString();
    User searchUser = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    log.debug("in unfollowUser findByFromUserNameAndToUserName : {}",
        followRepository.findByFromUserNameAndToUserName(currentUsername, username));
    followRepository.findByFromUserNameAndToUserName(currentUsername, username).ifPresent(
        follow -> followRepository.deleteByFromUserNameAndToUserName(currentUsername, username));
    return new ResponseProfile(userMapper.userToProfile(searchUser, false));
  }
}
