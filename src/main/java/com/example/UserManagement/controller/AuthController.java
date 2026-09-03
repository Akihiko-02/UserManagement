package com.example.UserManagement.controller;

import com.example.UserManagement.Dto.LoginRequestDto;
import com.example.UserManagement.Dto.LoginResponseDto;
import com.example.UserManagement.Dto.RegisterRequestDto;
import com.example.UserManagement.Dto.UserDto;
import com.example.UserManagement.entity.User;
import com.example.UserManagement.repository.UserRepository;
import com.example.UserManagement.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/registernormaluser")
    public ResponseEntity<UserDto> registerNormalUser(@RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authenticationService.registerNormalUser(registerRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authenticationService.login(loginRequestDto);
        ResponseCookie cookie = ResponseCookie.from("JWT",loginResponseDto.getJwtToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(1 * 60 * 60)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponseDto.getUserDto());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return authenticationService.logout();
    }

    @GetMapping("/getcurrentuser")
    public ResponseEntity<?> getCurrentUser(Authentication authentication){
        if(authentication == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not Authenticated");
        }
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("user not found."));

        return ResponseEntity.ok(convertToUserDTO(user));
    }

    public UserDto convertToUserDTO(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }


}
