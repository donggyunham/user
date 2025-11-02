package com.example.user.controller;

import com.example.user.dto.UserApiResponse;
import com.example.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// http://localhost:8095/api/user/
// method :리퀘스트 종류(방법) : GET, POST(Form), PUT(입력), DELETE(삭제), UPDATE(수정), ...
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;


    /**
     * health check : 서버가 동작하는지 여부를 확인하기 위한 리퀘스트 처리부
     * @return ResponseEntity : message에 동작중임을 알리는 내용이 전달된다.
     */
    @GetMapping("/health")  //http://localhost:8095/api/user/health
    public ResponseEntity<UserApiResponse<String>> healthCheck(){
        String res = "User 서비스가 동작중 입니다.";
        UserApiResponse<String> apiResponse = new UserApiResponse<>("success", res, null);
        return ResponseEntity.ok(apiResponse);  // response code : 200
    }
    // sign up : 회원가입 리퀘스트 처리부
    // login : 로그인 리퀘스트 처리부
    // logout : 로그아웃 리퀘스트 처리부
    // session : 세션 확인(로그인 확인) 처리부
    // 로그인한 사용자만 접근 가능한 리퀘스트 테스트
}
