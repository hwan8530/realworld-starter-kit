package com.realworld.backend.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.realworld.backend.user.dto.RequestUser;
import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.dto.RequestUser.Register;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class UserGroupControllerTest {

  @Autowired
  protected MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @Transactional
  @DisplayName("사용자 등록 테스트")
  void register() throws Exception {
    // Given
    RequestUser<Register> requestRegister = new RequestUser(
        new Register("test", "test@gmail.com", "testPassword"));
    // When
    ResultActions result = mockMvc.perform(
        post("/api/users/").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestRegister)));
    // Then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.user.username").value("test"))
        .andExpect(jsonPath("$.user.email").value("test@gmail.com"))
        .andExpect(jsonPath("$.user.token").isNotEmpty())
        .andDo(print());
  }

  @Test
  @DisplayName("등록된 사용자 로그인 테스트")
  void login() throws Exception {
    // Given
    RequestUser<Login> requestUser = new RequestUser<>(new Login("john@gmail.com", "john's"));
    // When
    ResultActions result = mockMvc.perform(
        post("/api/users/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestUser)));
    // Then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.user.username").value("john"))
        .andExpect(jsonPath("$.user.email").value("john@gmail.com"))
        .andExpect(jsonPath("$.user.token").isNotEmpty())
        .andDo(print());
  }
}