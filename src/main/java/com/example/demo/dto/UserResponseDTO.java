package com.example.demo.dto;

import java.util.Set;

public class UserResponseDTO {
    private long id;
    private String name;
    private String lastName;
    private String email;
    private int age;
    private Set<RoleDTO> roles;

    public UserResponseDTO(long id, String name, String lastName, String email, int age, Set<RoleDTO> roles) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
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
