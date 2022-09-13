package com.mycustomblog.blog.repository;


import com.mycustomblog.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //특정 게시글에 달린 댓글 조회
    @Query(value = "select * from comment where tier = 0 and articlenum = ?1", nativeQuery = true)
    List<Comment> findCommentsByArticlenum(Long articlenum);
    //최신 댓글
    List<Comment> findTop5ByOrderByCreatedDateDesc();
}
