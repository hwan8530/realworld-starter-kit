package com.realworld.backend.user.service;

import com.realworld.backend.common.errorhandling.handler.CustomException;
import com.realworld.backend.common.errorhandling.handler.CustomExceptionList;
import com.realworld.backend.common.security.jwt.JwtUtil;
import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.dto.RequestUser.Register;
import com.realworld.backend.user.dto.ResponseUser;
import com.realworld.backend.user.entity.User;
import com.realworld.backend.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;

  public ResponseUser login(Login login) {
    Optional<User> user = userRepository.findByEmail(login.getEmail());
    String username = user.orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND)).getUsername();
    String bio = user.orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND)).getBio();
    String image = user.orElseThrow(() -> new CustomException(CustomExceptionList.USER_NOT_FOUND)).getImage();
    String token = jwtUtil.createAuthToken(login);

    return new ResponseUser(login.getEmail(), token, username, bio, image);
  }

  public ResponseUser registration(Register register) {
    if (userRepository.existsById(register.getUsername())) {
      throw new CustomException(CustomExceptionList.FORBIDDEN_REQUEST);
    }
    userRepository.save(new User(register.getUsername(), register.getPassword(), register.getEmail()));
    String token = jwtUtil.createAuthToken(new Login(register.getEmail(), register.getPassword()));
    return new ResponseUser(register.getEmail(), token, register.getUsername(), null, null);
  }

  public ResponseUser getUser() {
    return null;
  }

}
