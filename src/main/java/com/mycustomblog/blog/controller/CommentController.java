package com.mycustomblog.blog.controller;

import com.mycustomblog.blog.config.auth.PrincipalImpl;
import com.mycustomblog.blog.domain.Member;
import com.mycustomblog.blog.dto.CommentDTO;
import com.mycustomblog.blog.dto.CommentVO;
import com.mycustomblog.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {
    @Autowired
    private final CommentService commentService = null;

    //댓글 목록
    @GetMapping("/comment/list/{articlenum}")
    @ResponseBody
    public List<CommentVO> getCommentList(@PathVariable Long articlenum){
        List<CommentVO> commentVOs = commentService.getCommentList(articlenum);
        return commentVOs;
    }

    //부모 댓글 작성
    @PostMapping("/comment/write")
    @ResponseBody
    public List<CommentVO> writeComment(@RequestParam Long articlenum,
                                        @RequestParam(required = false) Long parentCommentnum,
                                        @RequestBody CommentDTO commentDto,
                                        Authentication authentication){
        PrincipalImpl principal = (PrincipalImpl) authentication.getPrincipal();
        Member member = principal.getMember();

        if(parentCommentnum == null) {
            commentService.saveParentComment(commentDto, member, articlenum);
        } else {
            commentService.saveChildComment(commentDto, member, articlenum, parentCommentnum);
        }

        List<CommentVO> commentList = commentService.getCommentList(articlenum);
        return commentList;
    }

    //자식 댓글 작성

    //댓글 삭제
    @PostMapping("/comment/delete")
    @ResponseBody
    public List<CommentVO> deleteComment(@RequestParam Long articlenum,
                                         @RequestParam Long commentnum) {
        commentService.deleteComment(commentnum);

        List<CommentVO> commentList = commentService.getCommentList(articlenum);
        return commentList;
    }

    //비밀 댓글
}
