package com.jsalva.gymsystem.service.impl;

import com.jsalva.gymsystem.entity.User;
import com.jsalva.gymsystem.repository.UserRepository;
import com.jsalva.gymsystem.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        List<String> usernames = userRepository.findUsernamesLike(baseUsername + "%");  // Use UserRepository

        if (usernames.isEmpty() || !usernames.contains(baseUsername)) {
            return baseUsername;
        }

        int counter = 1;
        String uniqueUserName;
        do {
            uniqueUserName = baseUsername + counter;
            counter++;
        } while (usernames.contains(uniqueUserName));

        return uniqueUserName;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Long countUsers() {
        return userRepository.count();
    }
}
