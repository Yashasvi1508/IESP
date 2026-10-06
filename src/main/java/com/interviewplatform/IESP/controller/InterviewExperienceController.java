package com.interviewplatform.IESP.controller;

import com.interviewplatform.IESP.entity.InterviewExperience;
import com.interviewplatform.IESP.service.InterviewExperienceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/experiences")
public class InterviewExperienceController {

    private final InterviewExperienceService service;

    public InterviewExperienceController(
            InterviewExperienceService service) {

        this.service = service;
    }

    @GetMapping
    public String viewExperiences(
            @RequestParam(required = false) String search,
            Model model) {

        model.addAttribute(
                "experiences",
                service.searchExperiences(search)
        );

        model.addAttribute("search", search);

        return "experiences";
    }

    @GetMapping("/add")
    public String showAddExperienceForm(Model model) {

        model.addAttribute(
                "experience",
                new InterviewExperience()
        );

        return "add-experience";
    }

    @PostMapping("/save")
    public String saveExperience(
            @ModelAttribute InterviewExperience experience,
            Authentication authentication) {

        service.saveExperience(
                experience,
                authentication.getName()
        );

        return "redirect:/experiences";
    }

    @GetMapping("/edit/{id}")
    public String showEditExperienceForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        InterviewExperience experience =
                service.getExperienceForEdit(
                        id,
                        authentication.getName()
                );

        model.addAttribute("experience", experience);

        return "edit-experience";
    }

    @PostMapping("/update/{id}")
    public String updateExperience(
            @PathVariable Long id,
            @ModelAttribute InterviewExperience experience,
            Authentication authentication) {

        service.updateExperience(
                id,
                experience,
                authentication.getName()
        );

        return "redirect:/experiences";
    }

    @GetMapping("/delete/{id}")
    public String deleteExperience(
            @PathVariable Long id,
            Authentication authentication) {

        service.deleteExperience(
                id,
                authentication.getName()
        );

        return "redirect:/experiences";
    }
}