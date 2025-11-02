package com.example.user.entity;

import com.example.user.dto.SignupUserDTO;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity     // jpa repository object를 통해서 데이터를 저장할 오브젝트임을 명시
@Table(name = "users")  // 테이블 지정
@Getter
@Setter
@NoArgsConstructor  // 파라미터가 없는 생성자
@AllArgsConstructor // 모든 필드를 입력으로 받는 생성자
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto-increment
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.ROLE_USER;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    // 자동으로 createdAt, updatedAt 관리
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Role {
        ROLE_ADMIN,
        ROLE_USER
    }

    public static User fromDTO(SignupUserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setNickname(dto.getNick_name());

        return user;
    }
}