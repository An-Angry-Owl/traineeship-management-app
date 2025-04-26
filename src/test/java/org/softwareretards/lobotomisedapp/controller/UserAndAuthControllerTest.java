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
class UserAndAuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserAndAuthController userAndAuthController;

    @Test
    void showRegistrationFormShouldAddEmptyUserDtoToModel() {
        Model model = mock(Model.class);

        String viewName = userAndAuthController.showRegistrationForm(model);

        verify(model, times(1)).addAttribute(eq("user"), any(UserDto.class));
        assertEquals("user/registration-form", viewName);
    }

    @Test
    void registerUserShouldAddCreatedUserToModelAndReturnConfirmationView() {
        Model model = mock(Model.class);
        UserDto userDto = new UserDto();
        UserDto createdUser = new UserDto();
        when(userService.createUser(userDto)).thenReturn(createdUser);

        String viewName = userAndAuthController.registerUser(userDto, model);

        verify(model, times(1)).addAttribute("user", createdUser);
        assertEquals("user/registration-confirmation", viewName);
    }

    @Test
    void showLoginFormShouldReturnLoginFormView() {
        String viewName = userAndAuthController.showLoginForm();

        assertEquals("user/login-form", viewName);
    }

    @Test
    void showLogoutConfirmationShouldReturnLogoutConfirmationView() {
        String viewName = userAndAuthController.showLogoutConfirmation();

        assertEquals("user/logout-confirmation", viewName);
    }

    @Test
    void showUserProfileShouldAddUserProfileToModelAndReturnProfileView() {
        Model model = mock(Model.class);
        String username = "testUser";
        UserDto userDto = new UserDto();
        when(userService.getProfile(username)).thenReturn(userDto);

        String viewName = userAndAuthController.showUserProfile(username, model);

        verify(model, times(1)).addAttribute("user", userDto);
        assertEquals("user/profile-view", viewName);
    }

    @Test
    void showUserProfileShouldThrowExceptionWhenUserNotFound() {
        Model model = mock(Model.class);
        String username = "nonExistentUser";
        when(userService.getProfile(username)).thenThrow(new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> userAndAuthController.showUserProfile(username, model));
    }
}
