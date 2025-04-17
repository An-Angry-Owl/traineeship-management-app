package org.softwareretards.lobotomisedapp.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.entity.enums.Role;
import org.softwareretards.lobotomisedapp.entity.user.User;
import org.softwareretards.lobotomisedapp.repository.user.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserShouldThrowExceptionWhenUsernameIsTaken() {
        UserDto userDto = new UserDto();
        userDto.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.createUser(userDto));
    }

    @Test
    void createUserShouldSaveUserWhenUsernameIsAvailable() {
        UserDto userDto = new UserDto();
        userDto.setUsername("newStudent");
        userDto.setRole(Role.valueOf("STUDENT"));

        when(userRepository.findByUsername("newStudent")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDto result = userService.createUser(userDto);

        assertEquals("newStudent", result.getUsername());
        assertEquals("STUDENT", result.getRole().name()); // Use name() for enum comparison
    }

    @Test
    void getProfileShouldReturnUserDtoWhenUserExists() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        UserDto result = userService.getProfile("existingUser");

        assertEquals("existingUser", result.getUsername());
    }

    @Test
    void getProfileShouldThrowExceptionWhenUserDoesNotExist() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getProfile("nonExistentUser"));
    }

    @Test
    void findByUsernameShouldReturnUserDtoWhenUserExists() {
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(user));

        UserDto result = userService.findByUsername("existingUser");

        assertEquals("existingUser", result.getUsername());
    }

    @Test
    void findByUsernameShouldReturnNullWhenUserDoesNotExist() {
        when(userRepository.findByUsername("nonExistentUser")).thenReturn(Optional.empty());

        UserDto result = userService.findByUsername("nonExistentUser");

        assertNull(result);
    }
}
