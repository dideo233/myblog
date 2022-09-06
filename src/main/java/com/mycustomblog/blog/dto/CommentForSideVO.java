package com.mycustomblog.blog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentForSideVO{
    private Long articlenum;
    private String content;

    @Builder
    public CommentForSideVO(Long articlenum, String content) {
        this.articlenum = articlenum;
        this.content = content;
    }
}
