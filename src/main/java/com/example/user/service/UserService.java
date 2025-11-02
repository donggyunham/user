package com.example.user.service;

import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;    // 자동 주입(AutoWired), @RequiredArgsConstructor과 같이 사용해야 자동주입.
    private final PasswordEncoder passwordEncoder;  // 자동 주입
}
