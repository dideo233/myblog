package com.mycustomblog.blog.repository;

import com.mycustomblog.blog.domain.ArticleTemp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTempRepository extends JpaRepository<ArticleTemp, Long> {
}
