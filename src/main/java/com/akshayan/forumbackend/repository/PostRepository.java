package com.akshayan.forumbackend.repository;

import com.akshayan.forumbackend.model.Category;
import com.akshayan.forumbackend.model.ForumUser;
import com.akshayan.forumbackend.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findAllByCategory(Category category);

    List<Post> findByForumUser(ForumUser forumUser);

}