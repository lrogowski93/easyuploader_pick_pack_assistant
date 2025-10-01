package com.demo.easyuploader_pick_pack_assistant.repository.jpa;

import com.demo.easyuploader_pick_pack_assistant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String token);
}