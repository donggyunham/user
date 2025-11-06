package com.example.user.service;

import com.example.user.dto.SignupUserDTO;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;    // 자동 주입(AutoWired), @RequiredArgsConstructor과 같이 사용해야 자동주입.
    private final PasswordEncoder passwordEncoder;  // 자동 주입

    /**
     *
     * @param signupUserDTO
     * @return
     */
    @Transactional
    public User signup(SignupUserDTO signupUserDTO) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(signupUserDTO.getEmail())) {
            throw new IllegalArgumentException(signupUserDTO.getEmail() + "는 이미 존재하는 이메일입니다.");
        }

        User user = User.fromDTO(signupUserDTO);

        // 플레인 패스워드를 암호화된 패스워드로 변환
        String encPassword = passwordEncoder.encode(signupUserDTO.getPassword());
        user.setPassword(encPassword);
        return userRepository.save(user);
    }
}
