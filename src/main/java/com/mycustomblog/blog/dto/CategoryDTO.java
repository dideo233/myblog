package com.mycustomblog.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private String title;
    private int tier;
    private int count;
}
