package com.company.service;

import com.company.entity.User;
import com.company.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;


    public User save(User user) {
        String password=passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        return userRepository.save(user);
    }
}
