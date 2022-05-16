package com.akshayan.forumbackend.mapper;
import com.akshayan.forumbackend.dto.PostRequest;
import com.akshayan.forumbackend.dto.PostResponse;
import com.akshayan.forumbackend.model.*;
import com.akshayan.forumbackend.repository.CommentRepository;
import com.akshayan.forumbackend.repository.VoteRepository;
import com.akshayan.forumbackend.service.AuthService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.Optional;

import static com.akshayan.forumbackend.model.VoteType.DOWNVOTE;
import static com.akshayan.forumbackend.model.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {


    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private AuthService authService;


    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "forumUser", source = "forumUser")
    public abstract Post map(PostRequest postRequest, Category category, ForumUser forumUser);

    @Mapping(target = "id", source = "postId")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "userName", source = "forumUser.username")
    @Mapping(target = "commentCount", expression = "java(commentCount(post))")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return Date.from(post.getCreatedDate()).toString();
    }

    boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }

    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndForumUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

}