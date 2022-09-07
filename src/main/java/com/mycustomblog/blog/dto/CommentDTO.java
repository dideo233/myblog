package com.mycustomblog.blog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentDTO {
    @Size(min = 1, max = 500)
    private String content;
}