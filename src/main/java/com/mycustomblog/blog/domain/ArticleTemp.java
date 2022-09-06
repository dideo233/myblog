package com.mycustomblog.blog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class ArticleTemp extends BaseTime {

    @Id
    @Column
    private Long articleTempNum;

    @Column(nullable = false, length = 10000)
    private String content;

    public ArticleTemp(String content) {
        this.articleTempNum = 1L;
        this.content = content;
    }
}
