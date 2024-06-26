package com.mycustomblog.blog.service;

import com.mycustomblog.blog.domain.Article;
import com.mycustomblog.blog.domain.Comment;
import com.mycustomblog.blog.domain.Member;
import com.mycustomblog.blog.dto.CommentDTO;
import com.mycustomblog.blog.dto.CommentForSideVO;
import com.mycustomblog.blog.dto.CommentVO;
import com.mycustomblog.blog.repository.ArticleRepository;
import com.mycustomblog.blog.repository.CommentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {
    @Autowired
    private final CommentRepository commentRepository = null;
    @Autowired
    private final ArticleRepository articleRepository = null;

    //댓글 가져오기
    //fix할 것 : 계층이 0인 댓글만 가져오기. 자식은 알아서 조인해서 반환할 테니
    public List<CommentVO> getCommentList(Long articlenum){
        List<Comment> commentList = commentRepository.findCommentsByArticlenum(articlenum);

        List<CommentVO> commentVOs = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentVO commentVO = CommentVO.builder().comment(comment).build(); //부모 댓글
            for(Comment cComment : comment.getChildCommentList()){ //부모 댓글에 자식 댓글 Set
                commentVO.getCommentVOs().add(CommentVO.builder().comment(cComment).build());
            }
            commentVOs.add(commentVO);
        }

        return commentVOs;
    }

    //부모 댓글 작성
    @Transactional
    public void saveParentComment(CommentDTO commentDto, Member member, Long articlenum){
        Article article = articleRepository.findByArticlenum(articlenum);
        Comment comment = Comment.builder()
                .article(article)
                .tier(0)
                .content(commentDto.getContent())
                .member(member)
                .build();
        commentRepository.save(comment);
    }

    //자식 댓글 작성
    @Transactional
    public void saveChildComment(CommentDTO commentDto, Member member, Long articlenum, Long parentCommentnum){
        Comment parentComment = commentRepository.findById(parentCommentnum).get();
        Article article = articleRepository.findByArticlenum(articlenum);
        Comment comment = Comment.builder()
                .article(article)
                .parent(parentComment)
                .tier(1)
                .content(commentDto.getContent())
                .member(member)
                .build();
        commentRepository.save(comment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentnum){
        commentRepository.deleteById(commentnum);
    }

    //최신 댓글
    public List<CommentForSideVO> recentCommentList(){
        List<Comment> commentList = commentRepository.findTop5ByOrderByCreatedDateDesc();
        List<CommentForSideVO> commentForSideVOs = new ArrayList<>();

        for(Comment comment : commentList){
            commentForSideVOs.add(CommentForSideVO.builder().articlenum(comment.getArticle().getArticlenum()).content(comment.getContent()).build());
        }
        return commentForSideVOs;
    }
}
