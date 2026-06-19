package com.realworld.backend.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.realworld.backend.user.dto.RequestUser;
import com.realworld.backend.user.dto.RequestUser.Login;
import com.realworld.backend.user.dto.RequestUser.UpdateRequest;
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
class UserControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  private WebApplicationContext context;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("현재 사용자 테스트")
  void getCurrentUser() throws Exception {
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
    // 후행 메소드 실행 시 헤더에 토큰 포함
    result = mockMvc.perform(get("/api/user").header("Authorization", "Bearer " + token));
    // Then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.user.username").value("bob"))
        .andExpect(jsonPath("$.user.email").value("bob@gmail.com"))
        .andExpect(jsonPath("$.user.token").value(token))
        .andDo(print());
  }

  @Test
  @DisplayName("사용자 업데이트 테스트")
  void updateUser() throws Exception {
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
    RequestUser<UpdateRequest> updateRequest = new RequestUser<>(
        new UpdateRequest("bob2@gmail.com", "bob2", "bob22", "bob's bio", "bob's image"));
    result = mockMvc.perform(put("/api/user").header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateRequest)));
    // Then
    result.andExpect(status().isOk())
        .andExpect(jsonPath("$.user.username").value("bob2"))
        .andExpect(jsonPath("$.user.email").value("bob2@gmail.com"))
        .andExpect(jsonPath("$.user.bio").value("bob's bio"))
        .andExpect(jsonPath("$.user.image").value("bob's image"))
        .andExpect(jsonPath("$.user.token").value(token))
        .andDo(print());

  }
}