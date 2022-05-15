package com.akshayan.forumbackend.service;

import com.akshayan.forumbackend.Exception.CategoryNotFoundException;
import com.akshayan.forumbackend.Exception.PostNotFoundException;
import com.akshayan.forumbackend.dto.PostRequest;
import com.akshayan.forumbackend.dto.PostResponse;
import com.akshayan.forumbackend.mapper.PostMapper;
import com.akshayan.forumbackend.model.Category;
import com.akshayan.forumbackend.model.ForumUser;
import com.akshayan.forumbackend.model.Post;
import com.akshayan.forumbackend.repository.CategoryRepository;
import com.akshayan.forumbackend.repository.ForumUserRepository;
import com.akshayan.forumbackend.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final ForumUserRepository forumUserRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Category category = categoryRepository.findByName(postRequest.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(postRequest.getCategoryName()));
        postRepository.save(postMapper.map(postRequest, category, authService.getCurrentUser()));
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId.toString()));
        List<Post> posts = postRepository.findAllByCategory(category);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        ForumUser forumUser = forumUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByForumUser(forumUser)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }
}
