package com.example.Rider_Co.controllers;

import com.example.Rider_Co.models.User;
import com.example.Rider_Co.serviceInterfaces.UserServiceInterface;
import com.example.Rider_Co.services.UserService;
import com.example.Rider_Co.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "true"
)

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
        String result = userService.registerUser(user);

        if ("Username already taken".equals(result)) {
            return ResponseEntity.status(409).body(result); // 409 Conflict
        } else if ("User registered successfully!".equals(result)) {
            return ResponseEntity.status(201).body(result); // 201 Created

        }
        else{
            return ResponseEntity.badRequest().body(result); // badrequest

        }

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {
        Optional<User> loggedInUser = userService.authenticate(user.getUsername(), user.getPassword());

        if (loggedInUser.isPresent()) {
            String token = jwtUtil.generateToken(user.getUsername(),loggedInUser.get().getRole());
            String role = loggedInUser.get().getRole();

            try{
                ResponseCookie tokenCookie = ResponseCookie.from("Token",token)
                        .maxAge( 60 * 60)
                        .secure(false)
                        .path("/")
                        .sameSite("Lax")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE,tokenCookie.toString());

                ResponseCookie roleCookie = ResponseCookie.from("Role",role)
                        .maxAge( 60 * 60)
                        .secure(false)
                        .path("/")
                        .sameSite("Lax")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE,roleCookie.toString());

                return  ResponseEntity.ok("cookie created correctly");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("error in creating the cookie");
            }

        } else {
            return ResponseEntity.status(404).body("Invalid username or password");
        }
    }
}