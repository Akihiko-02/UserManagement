package com.example.UserManagement.controller;

import com.example.UserManagement.Dto.ChangePasswordDto;
import com.example.UserManagement.Dto.UserDto;
import com.example.UserManagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getuserbyid/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }
    @GetMapping("/getuserbyusername/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
    @GetMapping("/getallusers")
    public ResponseEntity<List<UserDto>> getAllUser(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/changepassword/{id}")
    public ResponseEntity<UserDto> changePassword(@PathVariable Long id, ChangePasswordDto changePasswordDto){
        return ResponseEntity.ok(userService.changePassword(id,changePasswordDto));
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,@RequestBody UserDto userDto){
        System.out.println("UPDATE USER CONTROLLER REACHED");
        System.out.println("UPDATE USER ID: " + id);
        System.out.println("UPDATE USER DATA: " + userDto.getUsername() + " / " + userDto.getEmail());
        return ResponseEntity.ok(userService.updateUser(id,userDto));
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
