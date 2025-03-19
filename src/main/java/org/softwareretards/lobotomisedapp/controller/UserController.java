package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.dto.user.UserDto;
import org.softwareretards.lobotomisedapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // US1: Registration
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "user/registration-form";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") UserDto userDto, Model model) {
        UserDto createdUser = userService.createUser(userDto);
        model.addAttribute("user", createdUser);
        return "user/registration-confirmation";
    }

    // US2: Login (view only - actual auth handled by Spring Security)
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login-form";
    }

    // US3: Logout (confirmation page)
    @GetMapping("/logout-success")
    public String showLogoutConfirmation() {
        return "user/logout-confirmation";
    }

    // Profile view
    @GetMapping("/profile/{username}")
    public String showUserProfile(@PathVariable String username, Model model) {
        UserDto userDto = userService.getProfile(username);
        model.addAttribute("user", userDto);
        return "user/profile-view";
    }
}