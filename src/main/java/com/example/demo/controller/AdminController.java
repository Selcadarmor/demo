package com.example.demo.controller;

import com.example.demo.model.Role;

import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

        @Autowired
        private UserService userService;

        @Autowired
        private RoleRepository roleRepository;


        @GetMapping
        public String adminHome(Model model, Principal principal) {
            String username = principal.getName();
            Optional<User> adminOpt = userService.findByUsername(username);
            adminOpt.ifPresent(admin -> model.addAttribute("currentUser", admin));
            model.addAttribute("users", userService.getAllUser());
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("user", new User());
            return "admin";
        }



        @PostMapping("/add")
        public String addUser(@ModelAttribute User user,
                              @RequestParam("roles") String[] roles) {
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : roles) {
                Role role = roleRepository.findByName(roleName);
                if (role != null) {
                    roleSet.add(role);
                }
            }
            user.setRoles(roleSet);
            userService.save(user);
            return "redirect:/admin";
        }


        @GetMapping("/edit/{id}")
        public String showEditForm(@PathVariable("id") Long id, Model model) {
            User user = userService.getUserById(id);
            model.addAttribute("user", user);
            model.addAttribute("users",userService.getAllUser());
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin";
        }


        @PostMapping("/update")
        public String updateUser(@ModelAttribute("user") User user,
                                 @RequestParam("roles") String[] roles) {
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : roles) {
                Role role = roleRepository.findByName(roleName);
                if (role != null) {
                    roleSet.add(role);
                }
            }
            user.setRoles(roleSet);
            userService.update(user);
            return "redirect:/admin";
        }


        @PostMapping("/delete/{id}")
        public String deleteUser(@PathVariable Long id) {
            userService.delete(id);
            return "redirect:/admin";
        }
    }

