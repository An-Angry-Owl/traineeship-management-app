package org.softwareretards.lobotomisedapp.controller;

import org.softwareretards.lobotomisedapp.entity.user.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String handleDashboardRedirect(@AuthenticationPrincipal User user) {
        return switch (user.getRole()) {
            case STUDENT -> "redirect:/students/" + user.getUsername() + "/dashboard";
            case COMPANY -> "redirect:/companies/" + user.getUsername() + "/dashboard";
            case PROFESSOR -> "redirect:/professors/" + user.getUsername() + "/dashboard";
            default -> "redirect:/";
        };
    }
}