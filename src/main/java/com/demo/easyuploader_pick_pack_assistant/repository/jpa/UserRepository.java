package com.demo.easyuploader_pick_pack_assistant.repository.jpa;

import com.demo.easyuploader_pick_pack_assistant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String token);

    @Query("SELECT u.login FROM User u WHERE u.id = ?1")
    Optional<String> findLoginById(Long userId);
}