package com.mycustomblog.blog.service;

import com.mycustomblog.blog.domain.Article;
import com.mycustomblog.blog.domain.Comment;
import com.mycustomblog.blog.domain.Member;
import com.mycustomblog.blog.dto.CommentDTO;
import com.mycustomblog.blog.dto.CommentVO;
import com.mycustomblog.blog.repository.ArticleRepository;
import com.mycustomblog.blog.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository = null;
    @Autowired
    private final ArticleRepository articleRepository = null;

    private final ModelMapper modelMapper = new ModelMapper();
    //댓글 가져오기
    public List<CommentVO> getCommentList(Long articlenum){
        List<Comment> commentList = commentRepository.findCommentsByArticlenum(articlenum);
        List<CommentVO> commentVOs = new ArrayList<>();

        for (Comment comment : commentList) {
            commentVOs.add(CommentVO.builder().comment(comment).build());
        }
        return commentVOs;
    }

    //댓글 작성
    public void saveParentComment(CommentDTO commentDto, Member member, Long articlenum){
        Article article = articleRepository.findByArticlenum(articlenum);
        Comment comment = Comment.builder()
                .article(article)
                .content(commentDto.getContent())
                .member(member)
                .build();
        commentRepository.save(comment);
    }
}
