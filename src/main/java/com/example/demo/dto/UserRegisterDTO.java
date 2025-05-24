package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


public class UserRegisterDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @Min(1)
    private int age;
     public UserRegisterDTO(String username, String password, String name, String lastName, String email, int age) {
         this.username = username;
         this.password = password;
         this.name = name;
         this.lastName = lastName;
         this.email = email;
         this.age = age;
     }
     public String getUsername() {
         return username;
     }
     public void setUsername(String username) {
         this.username = username;
     }
     public String getPassword() {
         return password;
     }
     public void setPassword(String password) {
         this.password = password;
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
}
