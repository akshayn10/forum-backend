package com.akshayan.forumbackend.repository;

import com.akshayan.forumbackend.model.Comment;
import com.akshayan.forumbackend.model.ForumUser;
import com.akshayan.forumbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);

    List<Comment> findAllByForumUser(ForumUser forumUser);
}
