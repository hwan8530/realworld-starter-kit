package com.realworld.backend.user.service;

import com.realworld.backend.common.errorhandling.handler.CustomException;
import com.realworld.backend.common.errorhandling.handler.CustomExceptionList;
import com.realworld.backend.common.security.jwt.JwtUtil;
import com.realworld.backend.mapper.UserMapper;
import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.dto.RequestUser.Register;
import com.realworld.backend.user.dto.RequestUser.UpdateRequest;
import com.realworld.backend.user.dto.ResponseUser;
import com.realworld.backend.user.dto.ResponseUser.ResponseUserDetails;
import com.realworld.backend.user.entity.User;
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
@Slf4j
@Transactional(readOnly = true)
public class UserService {

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public ResponseUser login(Login login) {
    User user = userRepository.findByEmail(login.getEmail())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    String token = jwtUtil.createAuthToken(login);
    ResponseUserDetails responseUser = userMapper.userToDetail(user);
    responseUser.setToken(token);
    return new ResponseUser(responseUser);
  }

  @Transactional
  public ResponseUser registration(Register register) {
    if (userRepository.existsByUsername(register.getUsername())) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }
    User user = new User(register.getUsername(), register.getPassword(), register.getEmail());
    userRepository.save(user);
    String token = jwtUtil.createAuthToken(new Login(register.getEmail(), register.getPassword()));
    ResponseUserDetails responseUser = userMapper.userToDetail(user);
    responseUser.setToken(token);
    return new ResponseUser(responseUser);
  }

  public ResponseUser getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }

    User user = userRepository.findByUsername(
            Objects.requireNonNull(authentication.getPrincipal()).toString())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    ResponseUserDetails userDetails = userMapper.userToDetail(user);
    userDetails.setToken(authentication.getCredentials().toString());
    return new ResponseUser(userDetails);
  }

  @Transactional
  public ResponseUser updateUser(UpdateRequest updateRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }
    User tokenUser = userRepository.findByUsername(authentication.getPrincipal().toString())
        .orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    tokenUser.setEmail(updateRequest.getEmail());
    tokenUser.setUsername(updateRequest.getUsername());
    tokenUser.setPassword(updateRequest.getPassword());
    tokenUser.setBio(updateRequest.getBio());
    tokenUser.setImage(updateRequest.getImage());

    ResponseUserDetails userDetails = userMapper.userToDetail(tokenUser);
    String token = jwtUtil.createAuthToken(
        new Login(tokenUser.getEmail(), tokenUser.getPassword()));
    userDetails.setToken(token);
    return new ResponseUser(userDetails);
  }

}
