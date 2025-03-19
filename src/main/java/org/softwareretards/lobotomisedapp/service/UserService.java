package org.softwareretards.lobotomisedapp.service;

import org.softwareretards.lobotomisedapp.dto.user.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto findByUsername(String username);
}