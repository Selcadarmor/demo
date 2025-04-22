package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public void save(User user) {
        userRepository.save(user);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id);
    }
    public void delete(Long id) {
        userRepository.delete(id);
    }
    public void update(User user) {
        userRepository.save(user);
    }
    public List<User> getAllUser() {
        return userRepository.findAllUser();
    }
}
