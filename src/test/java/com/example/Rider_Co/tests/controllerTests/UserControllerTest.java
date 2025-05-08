package com.example.Rider_Co.tests.controllerTests;

import com.example.Rider_Co.models.Rider;
import com.example.Rider_Co.models.User;
import com.example.Rider_Co.repositories.UserRepository;
import com.example.Rider_Co.services.UserService;
import com.example.Rider_Co.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @Autowired
   private UserRepository userRepository;

   @MockBean
   private UserService userService;

   @MockBean
   private JwtUtil jwtUtil;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @BeforeEach
   void setUp() {
      // Nothing needed here since Spring handles context setup
   }

   @Test
   @DirtiesContext
   void loginUserSuccess() throws Exception {
      User user = new User();
      user.setUsername("test1");
      user.setPassword(passwordEncoder.encode("test1")); // Use encoded password
      user.setRole("RIDER");
      userRepository.save(user);

      User loginRequest = new User();
      loginRequest.setUsername("test1");
      loginRequest.setPassword("test1"); // Raw password sent by client
      loginRequest.setRole("RIDER");

      System.out.println(">>> Request User: " + loginRequest);
      System.out.println(">>> Stored User: " + user);

      mockMvc.perform(post("/user/login")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(loginRequest)))
              .andExpect(status().isOk());
   }



   @Test
   void registerUserSuccess() throws Exception {
      User user = User.builder().username("ash").password("ash123").role("DRIVER").build();
      when(userService.registerUser(any(User.class))).thenReturn("User registered successfully!");

      mockMvc.perform(post("/user/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(user)))
              .andExpect(status().isCreated())
              .andExpect(content().string("User registered successfully!"));
   }

   @Test
   void registerUserAlreadyExists() throws Exception {
      User user = User.builder().username("ash").password("ash123").role("DRIVER").build();
      when(userService.registerUser(any(User.class))).thenReturn("Username already taken");

      mockMvc.perform(post("/user/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(user)))
              .andExpect(status().isConflict())
              .andExpect(content().string("Username already taken"));
   }

   @Test
   void registerWithEmptyPayload() throws Exception {
      mockMvc.perform(post("/user/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{}"))
              .andExpect(status().isBadRequest());
   }

   @Test
   void loginUserNotFound() throws Exception {
      User user = User.builder()
              .username("nonexistent")
              .password("wrongpassword")
              .build();

      when(userService.authenticate("nonexistent", "wrongpassword")).thenReturn(Optional.empty());

      mockMvc.perform(post("/user/login")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(user)))
              .andExpect(status().isNotFound())
              .andExpect(content().string("Invalid username or password"));
   }

   @Test
   void loginWithMalformedJson() throws Exception {
      String malformedJson = "{ \"username\": \"ash\", \"password\": \"ash123\""; // Missing closing brace

      mockMvc.perform(post("/user/login")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(malformedJson))
              .andExpect(status().isBadRequest());
   }

   @Test
   void registerWithMalformedJson() throws Exception {
      String malformedJson = "{\"username\": \"ash\", \"password\": \"ash123\", \"role\": \"RIDER\"";

      mockMvc.perform(post("/user/register")
                     .contentType(MediaType.APPLICATION_JSON)
              .content(malformedJson))
              .andExpect(status().isBadRequest());
   }



   @Test
   void registerWithInvalidInput() throws Exception {
      User user = User.builder()
              .username(null)
              .password("password")
              .role("DRIVER")
              .build();

      when(userService.registerUser(any(User.class))).thenReturn("Invalid user input");

      mockMvc.perform(post("/user/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(user)))
              .andExpect(status().isBadRequest())
              .andExpect(content().string("Invalid user input"));
   }



}
