package com.realworld.backend.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.realworld.backend.user.dto.RequestUser;
import com.realworld.backend.user.dto.RequestUser.Login;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("자기 자신 프로필 조회")
  void getProfile() throws Exception {
    // Given
    // login -> getCurrentUser
    RequestUser<Login> requestUser = new RequestUser<>(new Login("bob@gmail.com", "bob11"));
    ResultActions result = mockMvc.perform(
        post("/api/users/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestUser)));
    // token 추출
    String token = JsonPath.read(
        result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),
        "$.user.token");
    // When
    result = mockMvc.perform(get("/api/profiles/bob").header("Authorization", "Bearer " + token));
    // Then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.profile.username").value("bob"))
        .andDo(print());
  }

  @Test
  @DisplayName("타인 프로필 조회")
  void getOthersProfile() throws Exception {
    // Given
    // login -> getCurrentUser
    RequestUser<Login> requestUser = new RequestUser<>(new Login("john@gmail.com", "john's"));
    ResultActions result = mockMvc.perform(
        post("/api/users/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestUser)));
    // token 추출
    String token = JsonPath.read(
        result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),
        "$.user.token");
    // When
    result = mockMvc.perform(get("/api/profiles/bob").header("Authorization", "Bearer " + token));
    // Then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.profile.username").value("bob"))
        .andDo(print());
  }

  @Test
  @DisplayName("프로필 팔로우 테스트")
  void followProfile() throws Exception {
    // Given
    // login -> getCurrentUser
    RequestUser<Login> requestUser = new RequestUser<>(new Login("john@gmail.com", "john's"));
    ResultActions result = mockMvc.perform(
        post("/api/users/login").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestUser)));
    // token 추출
    String token = JsonPath.read(
        result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString(),
        "$.user.token");
    // When
    result = mockMvc.perform(
        post("/api/profiles/bob/follow").header("Authorization", "Bearer " + token));
    // Then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.profile.username").value("bob"))
        .andExpect(jsonPath("$.profile.following").value(true))
        .andDo(print());
  }

  @Test
  void unfollowProfile() {
  }
}