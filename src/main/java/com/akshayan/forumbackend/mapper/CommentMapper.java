package com.akshayan.forumbackend.mapper;

import com.akshayan.forumbackend.dto.CommentsDto;
import com.akshayan.forumbackend.model.Comment;
import com.akshayan.forumbackend.model.ForumUser;
import com.akshayan.forumbackend.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "forumUser", source = "forumUser")
    Comment map(CommentsDto commentsDto, Post post, ForumUser forumUser);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "userName", expression = "java(comment.getForumUser().getUsername())")
    CommentsDto mapToDto(Comment comment);
}