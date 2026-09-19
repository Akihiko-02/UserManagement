package com.example.UserManagement.service;

import com.example.UserManagement.Dto.ChangePasswordDto;
import com.example.UserManagement.Dto.UserDto;
import com.example.UserManagement.entity.User;
import com.example.UserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        return convertToUserDTO(user);
    }

    public UserDto getUserByUsername(String username){
        User user =  userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User not found"));
        return convertToUserDTO(user);
    }

    public List<UserDto> getAllUsers(){
        List<User> listOfUsers = userRepository.findAll();
        return listOfUsers.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDto changePassword(Long id, ChangePasswordDto changePasswordDto){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())){
            throw new RuntimeException("current Password is incorrect");
        }
        if(!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())){
            throw new RuntimeException("new passwords and confirm password don't match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        User savedUser = userRepository.save(user);


        System.out.println("Password is changed!!");
        return convertToUserDTO(savedUser);
    }

    public UserDto updateUser(Long id, UserDto userDto){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        User savedUser = userRepository.save(user);
        return convertToUserDTO(savedUser);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public UserDto convertToUserDTO(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());

        return userDto;
    }

}

