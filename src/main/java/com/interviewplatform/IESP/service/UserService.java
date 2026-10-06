package com.interviewplatform.IESP.service;

import com.interviewplatform.IESP.entity.User;
import com.interviewplatform.IESP.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setRole("USER");

        userRepository.save(user);
    }

    public java.util.List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public void blockUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setBlocked(true);
        userRepository.save(user);
    }

    public void unblockUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("User not found"));

        user.setBlocked(false);
        userRepository.save(user);
    }

    public long getTotalUsers(){
        return userRepository.count();
    }
    public long getBlockedUsers(){
        return userRepository.findAll()
                .stream()
                .filter(User::isBlocked)
                .count();
    }
    public void updateUser(Long id, String name,
                           String college, Integer graduationYear) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setName(name);
        user.setCollege(college);
        user.setGraduationYear(graduationYear);

        userRepository.save(user);
    }
    
}