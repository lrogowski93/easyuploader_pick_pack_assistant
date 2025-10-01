package com.demo.easyuploader_pick_pack_assistant.service;

import com.demo.easyuploader_pick_pack_assistant.model.User;
import com.demo.easyuploader_pick_pack_assistant.repository.jpa.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    UserRepository userRepository;

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
