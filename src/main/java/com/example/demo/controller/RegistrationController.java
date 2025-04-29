package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserService userService;
    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
         model.addAttribute("user", new User());
         return "register";
    }
    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute User user, Model model) {
         String result = userService.save(user);
         model.addAttribute("message", result);
         if (result.equals("User registered successfully")) {
             return "redirect:/login";
         }
         return "register";
    }
}
