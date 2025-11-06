package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     *
     * @param email
     * @return
     */
    Optional<User> findByEmail(String email); // email 정보를 주어 해당 사용자를 찾는 함수
    boolean existsByEmail(String email);
}
