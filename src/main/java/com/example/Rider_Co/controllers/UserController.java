package com.example.Rider_Co.controllers;

import com.example.Rider_Co.models.User;
import com.example.Rider_Co.serviceInterfaces.UserServiceInterface;
import com.example.Rider_Co.services.UserService;
import com.example.Rider_Co.jwtUtils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceInterface userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return ResponseEntity.status(201).body(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> loggedInUser = userService.authenticate(user.getUsername(), user.getPassword());

        if (loggedInUser.isPresent()) {
            String token = jwtUtil.generateToken(user.getUsername());
            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
