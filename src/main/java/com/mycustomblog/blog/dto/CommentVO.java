package com.mycustomblog.blog.dto;

import com.mycustomblog.blog.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CommentVO {
    private Long commentnum;
    private String content;
    private Long usernum;
    private String username;
    private String picUrl;
    private List<CommentVO> commentVOs = new ArrayList<>();
    private boolean secret;
    private LocalDateTime createdDate;

    @Builder
    public CommentVO(Comment comment) {
        this.commentnum = comment.getCommentnum();
        this.usernum = comment.getMember().getUsernum();
        this.username = comment.getMember().getUsername();
        this.picUrl = comment.getMember().getPicUrl();
        this.content = comment.getContent();
        this.secret = comment.isSecret();
        this.createdDate = comment.getCreatedDate();
    }

    //무한 계층형
//    public static List<CommentVO> createFrom(List<Comment> commentList, int dept) {
//        ArrayList<CommentVO> commentVOs = new ArrayList<>();
//
//        while (true) {
//            //댓글이 존재하지 않는 경우
//            if (commentList.isEmpty()) {
//                return commentVOs;
//            }
//
//            //댓글이 존재하는 경우
//            Comment comment = commentList.get(0);
//            if (comment.getTier() == dept) { //부모 댓글
//                commentVOs.add(new CommentVO(comment));
//                commentList.remove(0);
//            } else if (comment.getTier() > dept) {//자식 댓글
//                List<CommentVO> childList = createFrom(commentList, dept + 1);
//                commentVOs.get(commentVOs.size()-1).setCommentVOs(childList);
//            } else {
//                return commentVOs;
//            }
//        }
//    }
}
