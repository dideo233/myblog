package com.mycustomblog.blog.repository;

import com.mycustomblog.blog.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    //일반 페이징(카테고리별)
    @Query(value = "SELECT article.* FROM article, category c where article.categorynum = c.categorynum AND c.title =?1 order by articlenum desc",
            nativeQuery = true)
    Page<Article> findByCategoryOrderByCreatedDateDesc(String category, Pageable pageable);
    //게시글 상세보기
    Article findByArticlenum(Long articlenum);
    //인기글
    List<Article> findTop6ByOrderByHitDesc();
    //최신글 (무한 페이징 구현 시에도 사용됨)
    Slice<Article> findByOrderByCreatedDateDesc(Pageable pageable);

    //카테고리별 게시글 개수
    //@Query(value = "SELECT count(*) FROM article, category c where article.categorynum = c.categorynum AND c.title =?1",
    //        nativeQuery = true)
    //int getTotalCntByCategory(String category);
}
