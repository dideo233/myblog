package com.mycustomblog.blog.service;

import com.mycustomblog.blog.domain.Article;
import com.mycustomblog.blog.domain.Member;
import com.mycustomblog.blog.dto.ArticleDTO;
import com.mycustomblog.blog.repository.ArticleRepository;
import com.mycustomblog.blog.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ArticleService {
    @Autowired
    private final MemberRepository memberRepository = null;
    @Autowired
    private final ArticleRepository articleRepository = null;

    //글 작성
    @Transactional
    public void writeArticle(ArticleDTO articleDTO) {
        Article article = createArticleFrom(articleDTO);
        articleRepository.save(article);
    }
    //ArticleDTO -> Article
    private Article createArticleFrom(ArticleDTO articleDTO) {
        Member member = memberRepository.findById(articleDTO.getUsernum()).orElseThrow(()->{
            throw new IllegalArgumentException("작성자를 확인할 수 없습니다");
        });
        return Article.builder().title(articleDTO.getTitle()).content(articleDTO.getContent()).member(member).build();
    }
}
