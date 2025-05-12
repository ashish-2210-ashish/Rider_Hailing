package com.example.Rider_Co.services;

import com.example.Rider_Co.models.User;
import com.example.Rider_Co.repositories.UserRepository;
import com.example.Rider_Co.serviceInterfaces.UserServiceInterface;
import com.example.Rider_Co.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil ;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public String registerUser(User user) {
        if (user == null ||
                user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                user.getRole() == null || user.getRole().trim().isEmpty()) {
            return "Invalid user input";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already taken";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User registered successfully!";
    }

    @Override
    public Optional<User> authenticate(String username, String rawPassword) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()));
    }

    @Override
    public ResponseEntity<?> handleLogin(User user, HttpServletResponse response) {
        Optional<User> loggedInUser = authenticate(user.getUsername(), user.getPassword());

        if (loggedInUser.isPresent()) {
            String token = jwtUtil.generateToken(user.getUsername(), loggedInUser.get().getRole());
            String role = loggedInUser.get().getRole();

            try {
                ResponseCookie tokenCookie = ResponseCookie.from("Token", token)
                        .maxAge(60 * 60)
                        .secure(false)
                        .path("/")
                        .sameSite("Lax")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, tokenCookie.toString());

                ResponseCookie roleCookie = ResponseCookie.from("Role", role)
                        .maxAge(60 * 60)
                        .secure(false)
                        .path("/")
                        .sameSite("Lax")
                        .build();
                response.addHeader(HttpHeaders.SET_COOKIE, roleCookie.toString());

                return ResponseEntity.ok("cookie created correctly");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("error in creating the cookie");
            }

        } else {
            return ResponseEntity.status(404).body("Invalid username or password");
        }
    }

}
