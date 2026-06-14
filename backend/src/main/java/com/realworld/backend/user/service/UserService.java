package com.realworld.backend.user.service;

import com.realworld.backend.common.errorhandling.handler.CustomException;
import com.realworld.backend.common.errorhandling.handler.CustomExceptionList;
import com.realworld.backend.common.security.jwt.JwtUtil;
import com.realworld.backend.mapper.UserMapper;
import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.dto.RequestUser.Register;
import com.realworld.backend.user.dto.RequestUser.UpdateRequest;
import com.realworld.backend.user.dto.ResponseUser;
import com.realworld.backend.user.entity.User;
import com.realworld.backend.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public ResponseUser login(Login login, String token) {
    User user = userRepository.findByEmail(login.getEmail())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    token = jwtUtil.createAuthToken(login);
    return new ResponseUser(userMapper.userToDetail(user));
  }

  public ResponseUser registration(Register register) {
    if (userRepository.existsByUsername(register.getUsername())) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }
    User user = new User(register.getUsername(), register.getPassword(), register.getEmail());
    userRepository.save(user);
    String token = jwtUtil.createAuthToken(new Login(register.getEmail(), register.getPassword()));
    return new ResponseUser(userMapper.userToDetail(user));
  }

  public ResponseUser getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }

    User user = userRepository.findByUsername(
            Objects.requireNonNull(authentication.getCredentials()).toString())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    return new ResponseUser(userMapper.userToDetail(user));
  }

  public ResponseUser updateUser(UpdateRequest updateRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }

    User user = userRepository.findByEmail(updateRequest.getEmail())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    user.setBio(updateRequest.getBio());
    user.setImage(updateRequest.getImage());
    userRepository.save(user);

    return new ResponseUser(userMapper.userToDetail(user));
  }

}
