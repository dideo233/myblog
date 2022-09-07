package com.mycustomblog.blog.domain;

import com.mycustomblog.blog.dto.ArticleDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
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
    @JoinColumn(name = "usernum", nullable = false)
    private Member member;
    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorynum", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE) //댓글 리스트
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    public Article(String title, String content, Member member, String thumbnailUrl, Category category) {
        this.title = title;
        this.content = content;
        this.member = member;
        this.hit = 0L;
        this.thumbnailUrl = thumbnailUrl;
        this.category = category;
    }

    public void modifyArticle(ArticleDTO articleDTO, Category category){
        this.content = articleDTO.getContent();
        this.title = articleDTO.getTitle();
        this.thumbnailUrl = articleDTO.getThumbnailUrl();
        this.category = category;
    }

    public void addHit(){
        this.hit++;
    }

}
