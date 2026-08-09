package com.example.UserManagement.service;

import com.example.UserManagement.Dto.LoginRequestDto;
import com.example.UserManagement.Dto.LoginResponseDto;
import com.example.UserManagement.Dto.RegisterRequestDto;
import com.example.UserManagement.Dto.UserDto;
import com.example.UserManagement.JWT.JwtService;
import com.example.UserManagement.entity.User;
import com.example.UserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private UserDto registerNormalUser(RegisterRequestDto registerRequestDto){
        if(userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        Set<String> set = new HashSet<String>();
        set.add("ROLE_USER");
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRoles(set);

        User savedUser = userRepository.save(user);
        return convertToUserDTO(savedUser);
    }

    public UserDto registerAdminUser(RegisterRequestDto registerRequestDto){
        if(userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()){
            throw new RuntimeException("Username already exists");
        }
        Set<String> set = new HashSet<String>();
        set.add("ROLE_ADMIN");
        User user = new User();
        user.setUsername(registerRequestDto.getUsername());
        user.setEmail(registerRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        user.setRoles(set);

        User savedUser = userRepository.save(user);
        return convertToUserDTO(savedUser);
    }
    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        User user = userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(()-> new RuntimeException("Username not found"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(), loginRequestDto.getPassword()
        ));
        String jwtToken = jwtService.generateToken(user);

        return LoginResponseDto.builder()
                .jwtToken(jwtToken)
                .userDto(convertToUserDTO(user))
                .build();
    }

    public ResponseEntity<String> logout(){
        ResponseCookie cookie = ResponseCookie.from("JWT","")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully.");

    }



    public UserDto convertToUserDTO(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

}
