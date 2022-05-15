package com.akshayan.forumbackend.repository;

import com.akshayan.forumbackend.model.ForumUser;
import com.akshayan.forumbackend.model.Post;
import com.akshayan.forumbackend.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findTopByPostAndForumUserOrderByVoteIdDesc(Post post, ForumUser currentUser);
}

