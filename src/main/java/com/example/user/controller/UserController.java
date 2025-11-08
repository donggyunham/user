package com.example.user.controller;

import com.example.user.dto.LoginUserDTO;
import com.example.user.dto.LoginUserResponseDTO;
import com.example.user.dto.SignupUserDTO;
import com.example.user.dto.UserApiResponse;
import com.example.user.entity.User;
import com.example.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// http://localhost:8095/api/user/
// method :리퀘스트 종류(방법) : GET, POST(Form), PUT(입력), DELETE(삭제), UPDATE(수정), ...
@RestController                 // 이 클래스를 RestController로 만듬 -> Response를 JSON 구조로 전달.
@RequiredArgsConstructor        // private final로 선언된 멤버 필드를 자동 주입.
@RequestMapping("/api/user")    // http://127.0.0.1:8095/api/user
public class UserController {
    private final UserService userService;


    /**
     * health check : 서버가 동작하는지 여부를 확인하기 위한 리퀘스트 처리부
     *
     * @return ResponseEntity : message에 동작중임을 알리는 내용이 전달된다.
     */
    @GetMapping("/health")  //http://localhost:8095/api/user/health
    public ResponseEntity<UserApiResponse<String>> healthCheck() {
        String res = "User 서비스가 동작중 입니다.";
        UserApiResponse<String> apiResponse = new UserApiResponse<>("success", res, null);
        return ResponseEntity.ok(apiResponse);  // response code : 200
    }

    /**
     * 회원가입 요청(request) 처리
     * @param signupUserDTO signupUserDTO
     * 아래와 같이 JSON형태로 전달한다.
     * {
     * "email": "test@gmail.com"
     * "password": "1234"
     * "nick_name: "홍길동"
     * }
     */
    @PostMapping("/signup")
    public ResponseEntity<UserApiResponse<String>> signup(@RequestBody SignupUserDTO signupUserDTO) {
        try {
            // 이메일 정보가 누락되었다면 에러 코드를 반환한다.
            if (signupUserDTO.getEmail() == null || signupUserDTO.getEmail().isEmpty()) {
                UserApiResponse<String> response = new UserApiResponse<>(
                        "error",
                        "이메일은 필수 필드입니다.",
                        null
                );
            }

            // 패스워드 정보가 누락되었다면 에러코드를 반환한다.
            if (signupUserDTO.getPassword() == null || signupUserDTO.getPassword().isEmpty()) {
                UserApiResponse<String> response = new UserApiResponse<>(
                        "error",
                        "패스워드는 필수 필드입니다.",
                        null
                );
            };

            // 닉네임 정보가 누락되었다면 에러코드를 반환한다.
            if (signupUserDTO.getNick_name() == null || signupUserDTO.getNick_name().isEmpty()) {
                UserApiResponse<String> response = new UserApiResponse<>(
                        "error",
                        "닉네임은 필수 필드입니다.",
                        null
                );
            };

            // 누락된 정보가 없다면 회원가입 처리를 userService에게 처리하게하고 결과를 반환받는다.
            User user = userService.signup(signupUserDTO);
            if (user.getEmail() != null && user.getNickname() != null) {
                UserApiResponse<String> response = new UserApiResponse<>(
                        "success",
                        "회원가입 성공.",
                        null
                );
                return ResponseEntity.ok(response);
            }

            UserApiResponse<String> response = new UserApiResponse<>(
                    "error",
                    "회원가입 실패",
                    null
            );
            return ResponseEntity.internalServerError().body(response);
        } catch (IllegalArgumentException e) {
            UserApiResponse<String> response = new UserApiResponse<>(
                    "error",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.internalServerError().body(response);
        } catch (Exception e) {
            UserApiResponse<String> response = new UserApiResponse<>(
                    "error",
                    "데이터베이스 저장중에 알 수 없는 에러가 발생했습니다.",
                    null
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * login : 로그인 리퀘스트 처리부
     * @param loginUserDTO
     * @param session
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO loginUserDTO, HttpSession session) {
        try{
            // loginUserDTO에 누락된 정보가 있는지 검사
            if (loginUserDTO.getEmail()==null || loginUserDTO.getEmail().isEmpty()){
                UserApiResponse<String> response = new UserApiResponse<>(
                        "error",
                        "이메일은 필수 필드입니다.",
                        null
                );
                return ResponseEntity.internalServerError().body(response);
            }
            // 패스워드 정보가 누락되었다면 에러코드를 반환한다.
            if (loginUserDTO.getPassword() == null || loginUserDTO.getPassword().isEmpty()) {
                UserApiResponse<String> response = new UserApiResponse<>(
                        "error",
                        "패스워드는 필수 필드입니다.",
                        null
                );
            }
            LoginUserResponseDTO dto = userService.login(loginUserDTO);

            // session 객체를 만들어서 서버와 클라이언트가 갖음
            session.setAttribute("loginUser", dto);

                UserApiResponse<LoginUserResponseDTO> response = new UserApiResponse<>(
                        "success",
                        "로그인 성공.",
                        dto
                );
                return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            UserApiResponse<String> response = new UserApiResponse<>(
                    "error",
                    "데이터베이스 저장중에 알 수 없는 에러가 발생했습니다.",
                    null
            );
            return ResponseEntity.internalServerError().body(response);
        }catch (Exception e){
            UserApiResponse<String> response = new UserApiResponse<>(
                    "error",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * logout : 로그아웃 리퀘스트 처리부
     * @param session
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();   // 현재 사용자 세션 클리어.
        return ResponseEntity.ok(new UserApiResponse<>("success", "로그아웃 되었습니다.", null));
    }

    /**
     * session : 세션 확인(로그인 확인) 처리부
     * session은 상태(state) 객체이다. <==========> stateless
     * @param session
     * @return
     */
    @GetMapping("/session")
    public ResponseEntity<?> checkSession(HttpSession session) {
        LoginUserResponseDTO userDTO = (LoginUserResponseDTO)session.getAttribute("loginUser");
        if (userDTO == null){
            // Http Error 401 : UnAuthorized
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        // 로그인 사용자
        String data = "사용자 데이터 : " + userDTO.getNickname() + "님, 반갑습니다.";
        return ResponseEntity.ok(new UserApiResponse<String>("success", "로그인 사용자", data));
    }

    // 로그인한 사용자만 접근 가능한 리퀘스트 테스트
}
