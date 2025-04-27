package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.user.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getProfile(String username);
    UserDto findByUsername(String username);
    void changePassword(String username, String currentPassword, String newPassword, String confirmPassword);
}