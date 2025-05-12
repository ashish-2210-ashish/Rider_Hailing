package com.example.Rider_Co.serviceInterfaces;

import com.example.Rider_Co.models.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserServiceInterface {
    String registerUser(User user);
    Optional<User> authenticate(String username, String rawPassword);

    ResponseEntity<?> handleLogin(User user, HttpServletResponse response);
}
