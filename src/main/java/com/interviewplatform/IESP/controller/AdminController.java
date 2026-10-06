package com.interviewplatform.IESP.controller;

import com.interviewplatform.IESP.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.interviewplatform.IESP.service.InterviewExperienceService;
import com.interviewplatform.IESP.service.QuestionService;
import com.interviewplatform.IESP.entity.User;
import com.interviewplatform.IESP.repository.UserRepository;
import org.springframework.security.core.Authentication;

@Controller
public class AdminController {

    private final UserService userService;
    private final InterviewExperienceService experienceService;
    private final QuestionService questionService;

    private final UserRepository userRepository;

    public AdminController(
            UserService userService,
            InterviewExperienceService experienceService,
            QuestionService questionService,
            UserRepository userRepository) {

        this.userService = userService;
        this.experienceService = experienceService;
        this.questionService = questionService;
        this.userRepository = userRepository;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        model.addAttribute("user", user);

        model.addAttribute("totalUsers",
                userService.getTotalUsers());

        model.addAttribute("blockedUsers",
                userService.getBlockedUsers());

        model.addAttribute("totalExperiences",
                experienceService.getTotalExperiences());

        model.addAttribute("totalQuestions",
                questionService.getTotalQuestions());

        return "admin-dashboard";
    }

    @GetMapping("/admin/users")
    public String viewUsers(Model model){
        model.addAttribute("users",userService.getAllUsers());
        return "admin-users";
    }

    @PostMapping("/admin/users/block/{id}")
    public String blockUser(@PathVariable Long id){
        userService.blockUser(id);
        return "redirect:/admin/users";
    }
    @PostMapping("/admin/users/unblock/{id}")
    public String unblockUser(@PathVariable Long id){
        userService.unblockUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/experiences")
    public String viewExperiences(Model model){
        model.addAttribute("experiences", experienceService.getAllExperiences());
        return "admin-experiences";
    }
    @PostMapping("/admin/experiences/delete/{id}")
    public String deleteExperienceByAdmin(@PathVariable Long id){
        experienceService.deleteExperienceByAdmin(id);
        return "redirect:/admin/experiences";
    }
    @GetMapping("/admin/questions")
    public String viewQuestions(Model model){
        model.addAttribute("questions",questionService.getAllQuestions());
        return "admin-questions";
    }
    @PostMapping("/admin/questions/delete/{id}")
    public String deleteQuestionByAdmin(@PathVariable Long id){
        questionService.deleteQuestionByAdmin(id);
        return "redirect:/admin/questions";
    }

}