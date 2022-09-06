package com.mycustomblog.blog.service;

import com.mycustomblog.blog.domain.ArticleTemp;
import com.mycustomblog.blog.dto.ArticleTempDTO;
import com.mycustomblog.blog.repository.ArticleTempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleTempService {
    @Autowired
    private final ArticleTempRepository articleTempRepository = null;

    //임시 저장
    public ArticleTemp saveArticleTemp(ArticleTempDTO articleTempDTO){
        ArticleTemp articleTemp = new ArticleTemp(articleTempDTO.getContent());
        articleTempRepository.save(articleTemp);
        return articleTemp;
    }

    //임시 저장된 글 get
    public Optional<ArticleTemp> getArticleTemp(){
        return articleTempRepository.findById(1L);
    }

    //임시 저장된 글 delete
    public void deleteArticleTemp(){
        Optional<ArticleTemp> deleteArticle = articleTempRepository.findById(1L);
        deleteArticle.ifPresent(articleTempRepository::delete); //존재한다면 delete
    }
}
