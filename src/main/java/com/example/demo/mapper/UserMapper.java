package com.example.demo.mapper;

import com.example.demo.dto.RoleDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.model.User;
import com.example.demo.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public  class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDTO toResponseDTO(User user) {
        Set<RoleDTO> roleDTOs = RoleMapper. convertToDTOSet(user.getRoles());

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getAge(),
                roleDTOs
        );
    }

    public static User toEntity(UserRequestDTO dto, Set<Role> roles, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setUsername(dto.getUsername());
        return user;
    }

    public static User toEntity(UserRegisterDTO dto, Role defaultRole, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(Set.of(defaultRole));
        return user;
    }


    public static void updateEntityFromUpdateDTO(User user, UserUpdateDTO dto, Set<Role> roles) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        user.setRoles(roles);
    }

}