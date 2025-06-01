package com.example.demo.controller;

import com.example.demo.dto.RoleDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/api/users")
public class AdminRestController {

    private final UserService userService;
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminRestController(UserService userService, RoleRepository roleRepository, RoleMapper roleMapper) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<RoleDTO>> getAllRoles() {
        Set<Role> roles = new HashSet<>(roleRepository.findAll());
        Set<RoleDTO> roleDTOs = roleMapper.convertToDTOSet(roles);
        return ResponseEntity.ok(roleDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO dto) {
        try {
            UserResponseDTO createdUser = userService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (RuntimeException e) {
            // Можно добавить тело с ошибкой, если нужно
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO dto,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .toList();
            return ResponseEntity.badRequest().body(Map.<String, List<String>>of("errors", errors));
        }
        try {
            UserResponseDTO updatedUser = userService.update(id, dto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.<String, String>of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
