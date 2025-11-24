package com.demo.easyuploader_pick_pack_assistant.service;

import com.demo.easyuploader_pick_pack_assistant.model.User;
import com.demo.easyuploader_pick_pack_assistant.repository.jpa.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
    public String getUserLoginById(Long userId) {
        return userRepository.findLoginById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public List<User> findAllPickPackers() {
        return userRepository.findAll();
    }
}
