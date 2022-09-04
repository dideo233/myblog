package com.mycustomblog.blog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Article extends BaseTime{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARTICLE_SEQ_GENERATOR")
    @Column
    private Long articlenum;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 10000)
    private String content;
    @Column(columnDefinition = "bigint default 0", nullable = false)
    private Long hit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usernum")
    private Member member;
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorynum")
    private Category category;

    @Builder
    public Article(String title, String content, Member member, String thumbnailUrl, Category category) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.hit = 0L;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
    }
}
