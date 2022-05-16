package com.akshayan.forumbackend.service;
import com.akshayan.forumbackend.Exception.ForumException;
import com.akshayan.forumbackend.Exception.PostNotFoundException;
import com.akshayan.forumbackend.dto.CommentsDto;
import com.akshayan.forumbackend.mapper.CommentMapper;
import com.akshayan.forumbackend.model.Comment;
import com.akshayan.forumbackend.model.ForumUser;
import com.akshayan.forumbackend.model.NotificationEmail;
import com.akshayan.forumbackend.model.Post;
import com.akshayan.forumbackend.repository.CommentRepository;
import com.akshayan.forumbackend.repository.ForumUserRepository;
import com.akshayan.forumbackend.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final ForumUserRepository forumUserRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) {
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getForumUser().getUsername() + " posted a comment on your post." + POST_URL);
        sendCommentNotification(message, post.getForumUser());
    }

    private void sendCommentNotification(String message, ForumUser forumUser) {
        mailService.sendMail(new NotificationEmail(forumUser.getUsername() + " Commented on your post", forumUser.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String userName) {
        ForumUser forumUser = forumUserRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByForumUser(forumUser)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new ForumException("Comments contains unacceptable language");
        }
        return false;
    }
}
