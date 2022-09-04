package com.mycustomblog.blog.repository;

import com.mycustomblog.blog.domain.Article;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    //페이징
    Slice<Article> findBy(Pageable pageable);
    //인기글
    List<Article> findTop6ByOrderByHitDesc();
    //최신글
    Slice<Article> findByOrderByCreatedDateDesc(Pageable pageable);
}
