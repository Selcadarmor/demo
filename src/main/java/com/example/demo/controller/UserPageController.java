package com.example.demo.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserPageController {

    @RequestMapping("/user")
    public String userPage() {
        return "user";
    }
}
