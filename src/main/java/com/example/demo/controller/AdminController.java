
package com.example.demo.controller;

import com.example.demo.model.Role;

import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
        if (adminOpt.isPresent()) {
            User user = adminOpt.get();
            model.addAttribute("user", user);
        } else {
            model.addAttribute("user", new User());
        }

        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("newUser", new User());
        model.addAttribute("formAction", "/admin/add");  // для нового пользователя
        return "admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, Principal principal) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin"; // Если пользователь не найден
        }
        String username = principal.getName();
        model.addAttribute("user", userService.findByUsername(username).orElse(new User()));
        model.addAttribute("editUser", user);
        //model.addAttribute("users", userService.getAllUser());
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("formAction", "/admin/update");  // для обновления пользователя
        return "admin";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute("editUser") User user,
                             BindingResult result,
                             @RequestParam("roles") Long[] roles) {
        if (result.hasErrors()) {
            return "admin";
        }

        Set<Role> roleSet = new HashSet<>();
        for (Long roleId : roles) {
            Role role = roleRepository.findById(roleId).orElse(null);
            if (role != null) {
                roleSet.add(role);
            }
        }

        user.setRoles(roleSet);
        userService.update(user);

        return "redirect:/admin";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("newUser") User user,
                          BindingResult result,
                          @RequestParam("roles") Long[] roles) {
        if (result.hasErrors()) {
            return "admin";
        }

        Set<Role> roleSet = new HashSet<>();
        for (Long roleId : roles) {
            Role role = roleRepository.findById(roleId).orElse(null);
            if (role != null) {
                roleSet.add(role);
            }
        }
        user.setRoles(roleSet);

        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (userService.existsById(id)) {
            userService.delete(id);
        }
        return "redirect:/admin";
    }
}
