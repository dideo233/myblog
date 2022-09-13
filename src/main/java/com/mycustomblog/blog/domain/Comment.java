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
public class Comment extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column
    private Long commentnum;
    @Column
    private String content;
    @Column
    private int tier;
    @Column(columnDefinition = "boolean default false")
    private boolean secret;

    //셀프조인 (계층형 댓글 구현용)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentnum")
    private Comment parent; //대댓글 시 달리는 comment는 해당 setParents로 넣어주기

    @OneToMany(mappedBy = "parent")
    private List<Comment> childCommentList = new ArrayList<>(); //그럼 이걸로 자식 comment 가져오기 가능

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articlenum")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usernum")
    private Member member;

    @Builder
    public Comment(Article article, Comment parent, int tier, Member member, String content, boolean secret) {
        this.article = article;
        this.parent = parent;
        this.member = member;
        this.content = content;
        this.tier = tier;
        this.secret = secret;
    }
}
