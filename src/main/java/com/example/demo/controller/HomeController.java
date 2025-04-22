package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @GetMapping(value="/")
    public String home(Model model) {
        List<String> mesages = new ArrayList<>();
        mesages.add("Hello, user!");
        mesages.add("Welcome to my first application!");
        model.addAttribute("messages", mesages);
        return "home";
    }
}

