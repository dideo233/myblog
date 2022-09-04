package com.mycustomblog.blog.repository;

import com.mycustomblog.blog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    //페이징(전체)
    Page<Article> findAllByOrderByCreatedDateDesc(Pageable pageable);
    //페이징(카테고리별)
    @Query(value = "SELECT article.* FROM article, category c where article.categorynum = c.categorynum AND c.title =?1 order by articlenum desc",
            nativeQuery = true)
    Page<Article> findByCategoryOrderByCreatedDateDesc(String category, Pageable pageable);
    //카테고리별 게시글 개수
    @Query(value = "SELECT count(*) FROM article, category c where article.categorynum = c.categorynum AND c.title =?1",
            nativeQuery = true)
    int getTotalCntByCategory(String category);
    //게시글 상세보기
    Article findByArticlenum(Long articlenum);
    //무한 스크롤
    Slice<Article> findBy(Pageable pageable);
    //인기글
    List<Article> findTop6ByOrderByHitDesc();
    //최신글
    Slice<Article> findByOrderByCreatedDateDesc(Pageable pageable);
}
