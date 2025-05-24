package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public class UserUpdateDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @Email
    private String email;
    @Min(1)
    private int age;
    @NotEmpty
    private Set<RoleDTO> roles;

    public UserUpdateDTO() {}

    public UserUpdateDTO(Long id, String name, String lastName, String email, int age, Set<RoleDTO> roles) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.roles = roles;
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
     public String getName() {
        return name;
     }
     public void setName(String name) {
        this.name = name;
     }
     public String getLastName() {
        return lastName;
     }
     public void setLastName(String lastName) {
        this.lastName = lastName;
     }
     public String getEmail() {
        return email;
     }
     public void setEmail(String email) {
        this.email = email;
     }
     public int getAge() {
        return age;
     }
     public void setAge(int age) {
        this.age = age;
     }
     public Set<RoleDTO> getRoles() {
        return roles;
     }
     public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
     }
}
