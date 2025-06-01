package com.example.demo.service;

import com.example.demo.dto.RoleDTO;
import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.dto.UserRegisterDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toResponseDTO);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUser() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserMapper::toResponseDTO);
    }


    @Transactional
    public UserResponseDTO save(UserRequestDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        Set<Role> roles = mapRolesFromDTOs(dto.getRoles());
        User user = UserMapper.toEntity(dto, roles, passwordEncoder);

        return UserMapper.toResponseDTO(userRepository.save(user));
    }


    @Transactional
    public UserResponseDTO update(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Role> roles = mapRolesFromDTOs(dto.getRoles());
        UserMapper.updateEntityFromUpdateDTO(user, dto, roles);

        return UserMapper.toResponseDTO(userRepository.save(user));
    }


    @Transactional
    public UserResponseDTO register(UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        User user = UserMapper.toEntity(dto, defaultRole, passwordEncoder);

        return UserMapper.toResponseDTO(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }


    private Set<Role> mapRolesFromDTOs(Set<RoleDTO> roleDTOs) {
        return roleDTOs.stream()
                .map(dto -> roleRepository.findById(dto.getId())
                        .orElseThrow(() -> new RuntimeException("Role not found: " + dto.getName())))
                .collect(Collectors.toSet());
    }
}
