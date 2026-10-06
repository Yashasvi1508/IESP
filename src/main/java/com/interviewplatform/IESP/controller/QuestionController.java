package com.interviewplatform.IESP.controller;

import com.interviewplatform.IESP.entity.Question;
import com.interviewplatform.IESP.service.QuestionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/questions")
public class QuestionController {

    private final QuestionService service;

    public QuestionController(QuestionService service) {
        this.service = service;
    }

    @GetMapping
    public String viewQuestions(
            @RequestParam(required = false) String search,
            Model model) {

        model.addAttribute(
                "questions",
                service.searchQuestions(search)
        );

        model.addAttribute("search", search);

        return "questions";
    }

    @GetMapping("/add")
    public String showAddQuestionForm(Model model) {

        model.addAttribute(
                "questionForm",
                new Question()
        );

        return "add-question";
    }

    @PostMapping("/save")
    public String saveQuestion(
            @ModelAttribute("questionForm") Question question,
            Authentication authentication) {

        service.saveQuestion(
                question,
                authentication.getName()
        );

        return "redirect:/questions";
    }

    @GetMapping("/edit/{id}")
    public String showEditQuestionForm(
            @PathVariable Long id,
            Authentication authentication,
            Model model) {

        Question question =
                service.getQuestionForEdit(
                        id,
                        authentication.getName()
                );

        model.addAttribute(
                "questionForm",
                question
        );

        return "edit-question";
    }

    @PostMapping("/update/{id}")
    public String updateQuestion(
            @PathVariable Long id,
            @ModelAttribute("questionForm") Question question,
            Authentication authentication) {

        service.updateQuestion(
                id,
                question,
                authentication.getName()
        );

        return "redirect:/questions";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuestion(
            @PathVariable Long id,
            Authentication authentication) {

        service.deleteQuestion(
                id,
                authentication.getName()
        );

        return "redirect:/questions";
    }
}