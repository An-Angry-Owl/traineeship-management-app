package org.softwareretards.lobotomisedapp.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void showRegistrationFormShouldAddEmptyUserDtoToModel() {
        Model model = mock(Model.class);

        String viewName = userController.showRegistrationForm(model);

        verify(model, times(1)).addAttribute(eq("user"), any(UserDto.class));
        assertEquals("user/registration-form", viewName);
    }

    @Test
    void registerUserShouldAddCreatedUserToModelAndReturnConfirmationView() {
        Model model = mock(Model.class);
        UserDto userDto = new UserDto();
        UserDto createdUser = new UserDto();
        when(userService.createUser(userDto)).thenReturn(createdUser);

        String viewName = userController.registerUser(userDto, model);

        verify(model, times(1)).addAttribute("user", createdUser);
        assertEquals("user/registration-confirmation", viewName);
    }

    @Test
    void showLoginFormShouldReturnLoginFormView() {
        String viewName = userController.showLoginForm();

        assertEquals("user/login-form", viewName);
    }

    @Test
    void showLogoutConfirmationShouldReturnLogoutConfirmationView() {
        String viewName = userController.showLogoutConfirmation();

        assertEquals("user/logout-confirmation", viewName);
    }

    @Test
    void showUserProfileShouldAddUserProfileToModelAndReturnProfileView() {
        Model model = mock(Model.class);
        String username = "testUser";
        UserDto userDto = new UserDto();
        when(userService.getProfile(username)).thenReturn(userDto);

        String viewName = userController.showUserProfile(username, model);

        verify(model, times(1)).addAttribute("user", userDto);
        assertEquals("user/profile-view", viewName);
    }

    @Test
    void showUserProfileShouldThrowExceptionWhenUserNotFound() {
        Model model = mock(Model.class);
        String username = "nonExistentUser";
        when(userService.getProfile(username)).thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> userController.showUserProfile(username, model));
    }
}
