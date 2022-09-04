package com.mycustomblog.blog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class ArticleDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private Long usernum;
}
