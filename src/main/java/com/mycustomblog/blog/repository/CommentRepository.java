package com.mycustomblog.blog.repository;

import com.mycustomblog.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select * from comment where articlenum = ?1", nativeQuery = true)
    List<Comment> findCommentsByArticlenum(Long articlenum);
}
