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

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
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
    public ResponseEntity<?> login(@RequestBody User user , HttpServletResponse response) {
        Optional<User> loggedInUser = userService.authenticate(user.getUsername(), user.getPassword());

        if (loggedInUser.isPresent()) {

            String username = user.getUsername();
            String role = loggedInUser.get().getRole();
            String token = jwtUtil.generateToken(username,role);

            try{
                ResponseCookie cookieToken =ResponseCookie.from("Token",token)

                        .maxAge(60*60)
                        .httpOnly(true)
                        .sameSite("Lax")
                        .secure(false)
                        .path("/")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookieToken.toString());

                ResponseCookie cookieRole =ResponseCookie.from("Role",role)

                        .maxAge(60*60)
                        .sameSite("Lax")
                        .secure(false)
                        .path("/")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookieRole.toString());

                return ResponseEntity.ok("login successfull\n");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("error in creating cookies ");
            }
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
