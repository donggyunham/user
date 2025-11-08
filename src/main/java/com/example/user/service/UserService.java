package com.example.user.service;

import com.example.user.dto.LoginUserDTO;
import com.example.user.dto.LoginUserResponseDTO;
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
        // SignupUserDTO instance를 이용하여 User Instance를 생성한다.
        User user = User.fromDTO(signupUserDTO);

        // 플레인 패스워드를 암호화된 패스워드로 변환
        String encPassword = passwordEncoder.encode(signupUserDTO.getPassword());
        user.setPassword(encPassword);  // user instance에 암호화된 비밀번호로 다시 설정.

        return userRepository.save(user);   // JPA에 의해서 자동으로 테이블에 저장.(insert into 쿼리가 자동으로 만들어져서 전달된다.)
    }

    public LoginUserResponseDTO login(LoginUserDTO loginUserDTO){
        User user =  userRepository.findByEmail(loginUserDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(loginUserDTO.getEmail() + " 사용자 정보가 존재하지 않습니다."));

        // 패스워드 검증
        boolean isEqual = passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword());
        if (!isEqual) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // User 인스턴스를 LoginUserResponseDTO 인스턴스로 변환하여 반환
        return User.toLoginuserResponse(user);
    }
}
