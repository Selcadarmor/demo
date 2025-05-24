package com.example.demo.mapper;

import com.example.demo.dto.RoleDTO;
import com.example.demo.model.Role;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
@Component
public class RoleMapper {

    public static RoleDTO convertToDTO(Role role) {

        return new RoleDTO(role.getId(), role.getName());
    }

    public  static Set<RoleDTO> convertToDTOSet(Set<Role> roles) {
        return roles.stream()
                .map(RoleMapper::convertToDTO).collect(Collectors.toSet());
    }
}
