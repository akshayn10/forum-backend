package com.akshayan.forumbackend.repository;

import com.akshayan.forumbackend.model.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumUserRepository extends JpaRepository<ForumUser, Long> {
    ForumUser findByUsername(String username);
    ForumUser findByEmail(String email);
}
