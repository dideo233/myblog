package com.mycustomblog.blog.dto;

import com.mycustomblog.blog.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CommentVO {
    private Long commentnum;
    private String content;
    private String username;
    private String picUrl;
    private boolean secret;
    private LocalDateTime createdDate;

    @Builder
    public CommentVO(Comment comment) {
        this.commentnum = comment.getCommentnum();
        this.username = comment.getMember().getUsername();
        this.picUrl = comment.getMember().getPicUrl();
        this.content = comment.getContent();
        this.secret = comment.isSecret();
        this.createdDate = comment.getCreatedDate();
    }
}
