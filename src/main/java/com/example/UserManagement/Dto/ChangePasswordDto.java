package com.example.UserManagement.Dto;

import lombok.Data;

@Data
public class ChangePasswordDto {

    private  String currentPassword;
    private  String newPassword;
    private  String confirmPassword;
}
