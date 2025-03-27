package com.example.Rider_Co.tests.controllerTests;

import com.example.Rider_Co.models.User;
import com.example.Rider_Co.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import com.example.Rider_Co.controllers.UserController;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

   private MockMvc mockMvc;

   @Mock
   private UserService userService;

   @InjectMocks
   private UserController userController;

   private ObjectMapper objectMapper;

   @BeforeEach
   public void setUp() {
      mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
      objectMapper = new ObjectMapper();
   }

   @Test
   public void registerUserSuccess() throws Exception {
      User user = User.builder()
              .username("ash")
              .password("ash123")
              .role("DRIVER")
              .build();

      Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn("User registered successfully!");

      mockMvc.perform(post("/user/register")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(user)))
              .andExpect(status().isCreated())
              .andExpect(content().string("User registered successfully!"));
   }


   @Test
   public void registerUserAreadyExists() throws  Exception{
      User user= User.builder()
              .username("ash")
              .password("ash123")
              .role("DRIVER")
              .build();

      Mockito.when(userService.registerUser(Mockito.any(User.class))).thenReturn("Username already taken");

      mockMvc.perform(post("/user/register")
              .contentType(MediaType.APPLICATION_JSON)
              .content(objectMapper.writeValueAsString(user)))
              .andExpect(content().string("Username already taken"));

   }

}