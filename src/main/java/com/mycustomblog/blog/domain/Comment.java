package com.mycustomblog.blog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column
    private Long commentnum;
    private String content;

    @Column(columnDefinition = "boolean default false")
    private boolean secret;

    //셀프조인 (계층형 댓글 구현 시 사용할 것)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "parentnum")
//    private Comment parents; //대댓글 시 달리는 comment는 해당 setParents로 넣어주기
//
//    @OneToMany(mappedBy = "parents")
//    private List<Comment> childCommentList = new ArrayList<>(); //그럼 이걸로 자식 comment 가져오기 가능

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articlenum")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usernum")
    private Member member;

    @Builder
    public Comment(Article article, Comment parents, Member member, String content, boolean secret) {
        this.article = article;
        this.member = member;
        this.content = content;
        this.secret = secret;
    }
}
