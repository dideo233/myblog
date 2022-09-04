package com.mycustomblog.blog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_SEQ_GENERATOR")
    @Column
    private Long categorynum;
    private String title;

    @OneToMany(mappedBy = "category")
    private List<Article> articles = new ArrayList<>();

    @Builder
    public Category(String title) {
        this.title = title;
    }
}
