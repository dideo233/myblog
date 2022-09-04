package com.mycustomblog.blog.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//뷰로 뿌리기 위한 객체
@Setter
@Getter
public class ArticleVO {
    private Long articlenum;
    private String title;
    private String content;
    private String thumbnailUrl;
    private LocalDateTime createdDate;
}
