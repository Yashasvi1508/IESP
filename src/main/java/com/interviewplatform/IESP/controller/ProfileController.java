package com.interviewplatform.IESP.controller;

import com.interviewplatform.IESP.entity.User;
import com.interviewplatform.IESP.repository.UserRepository;
import com.interviewplatform.IESP.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final UserService userService;

    public ProfileController(UserRepository userRepository,
                             UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/profile/edit")
    public String editProfile(Authentication authentication,
                              Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        model.addAttribute("user", user);

        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            Authentication authentication,
            @RequestParam String name,
            @RequestParam String college,
            @RequestParam Integer graduationYear) {

        String currentEmail = authentication.getName();

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        userService.updateUser(
                user.getId(),
                name,
                college,
                graduationYear
        );

        return "redirect:/profile/edit?success";
    }
}