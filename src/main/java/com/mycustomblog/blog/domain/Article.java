package com.mycustomblog.blog.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Article extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_generator")
    @Column
    private Long articlenum;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    private Long hit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usernum")
    private Member member;
    private String thumbnailUrl;

    @Builder
    public Article(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.hit = 0L;
    }
}
