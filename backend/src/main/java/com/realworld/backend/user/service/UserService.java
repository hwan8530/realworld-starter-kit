package com.realworld.backend.user.service;

import com.realworld.backend.common.errorhandling.handler.CustomException;
import com.realworld.backend.common.errorhandling.handler.CustomExceptionList;
import com.realworld.backend.common.security.jwt.JwtUtil;
import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.dto.RequestUser.Register;
import com.realworld.backend.user.dto.RequestUser.UpdateRequest;
import com.realworld.backend.user.dto.ResponseUser;
import com.realworld.backend.user.dto.ResponseUser.ResponseUserDetails;
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

  public ResponseUser login(Login login) {
    User user = userRepository.findByEmail(login.getEmail()).orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    String token = jwtUtil.createAuthToken(login);
    return new ResponseUser(new ResponseUserDetails(user.getEmail(), token, user.getUsername(), user.getBio(), user.getImage()));
  }

  public ResponseUser registration(Register register) {
    if (userRepository.existsById(register.getUsername())) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }

    userRepository.save(new User(register.getUsername(), register.getPassword(), register.getEmail()));
    String token = jwtUtil.createAuthToken(new Login(register.getEmail(), register.getPassword()));
    return new ResponseUser(new ResponseUserDetails(register.getEmail(), token, register.getUsername(), null, null));
  }

  public ResponseUser getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }

    User user = userRepository.findById(Objects.requireNonNull(authentication.getCredentials()).toString()).orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    return new ResponseUser(new ResponseUserDetails(user.getEmail(), Objects.requireNonNull(authentication.getPrincipal()).toString(), user.getUsername(), user.getBio(), user.getImage()));
  }

  public ResponseUser updateUser(UpdateRequest updateRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }

    User user = userRepository.findByEmail(updateRequest.getEmail()).orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND));
    user.setBio(updateRequest.getBio());
    user.setImage(updateRequest.getImage());
    userRepository.save(user);

    return new ResponseUser(new ResponseUserDetails(user.getEmail(), Objects.requireNonNull(authentication.getPrincipal()).toString(),
        user.getUsername(), user.getBio(), user.getImage()));
  }

}
