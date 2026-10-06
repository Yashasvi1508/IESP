package com.interviewplatform.IESP.controller;

import com.interviewplatform.IESP.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService){
        this.chatService= chatService;
    }

    @GetMapping("/chat")
    public String chatPage(){
        return "chat";
    }

    @PostMapping("/chat")
    public String askAI(@RequestParam("message") String message, Model model){
        String response = chatService.askAI(message);
        model.addAttribute("message" , message);
        model.addAttribute("response", response);

        return "chat";
    }
}
