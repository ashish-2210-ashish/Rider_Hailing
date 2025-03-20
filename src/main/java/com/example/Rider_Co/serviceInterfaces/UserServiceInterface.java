package com.example.Rider_Co.serviceInterfaces;

import com.example.Rider_Co.models.User;

import java.util.Optional;

public interface UserServiceInterface {
    String registerUser(User user);
    Optional<User> authenticate(String username, String rawPassword);
}
