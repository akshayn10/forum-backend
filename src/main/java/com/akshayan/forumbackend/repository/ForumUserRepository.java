package com.akshayan.forumbackend.repository;

import com.akshayan.forumbackend.model.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumUserRepository extends JpaRepository<ForumUser, Long> {
    Optional<ForumUser> findByUsername(String username);
    ForumUser findByEmail(String email);
}
