package com.jsalva.gymsystem.service;

import com.jsalva.gymsystem.repository.UserRepository;

public interface AuthService {
    String login(String username, String password);

}
