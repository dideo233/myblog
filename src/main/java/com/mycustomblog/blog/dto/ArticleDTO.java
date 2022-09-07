package com.mycustomblog.blog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
public class ArticleDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private Long usernum;
    private String thumbnailUrl;
    @NotBlank
    private String category;

    @Builder
    public ArticleDTO(String title, String content, Long usernum, String thumbnaulUrl, String category) {
        this.title = title;
        this.content = content;
        this.usernum = usernum;
        this.category = category;
        this.thumbnailUrl = thumbnaulUrl;
    }

}
