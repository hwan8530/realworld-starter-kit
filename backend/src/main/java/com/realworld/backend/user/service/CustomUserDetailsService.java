package com.realworld.backend.user.service;
import com.realworld.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return new CustomUserDetails(userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("username is not found")));
  }

  public CustomUserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
    return new CustomUserDetails(userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("email is not found")));
  }
}
