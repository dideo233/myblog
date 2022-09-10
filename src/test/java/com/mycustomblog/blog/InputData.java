package com.mycustomblog.blog;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.mycustomblog.blog.domain.Article;
import com.mycustomblog.blog.dto.ArticleDTO;
import com.mycustomblog.blog.repository.ArticleRepository;
import com.mycustomblog.blog.repository.CategoryRepository;
import com.mycustomblog.blog.repository.MemberRepository;
import com.mycustomblog.blog.service.CategoryService;
import com.mycustomblog.blog.service.PrincipalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

//@SpringBootTest
@Transactional
@Rollback(value = false)
public class InputData {
    @Autowired
    EntityManager entityManager;
    @Autowired
    PrincipalServiceImpl principalServiceImpl;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    MemberRepository memberRepository;
	@Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryService categoryService;
    
    //@BeforeEach //Test 메서드 실행 이전에 실행
    void dummyDataCreate() {
    	principalServiceImpl.insertAdmin(); //관리자 계정 생성
    	
    	ArticleDTO articleDTO1 = new ArticleDTO();
    	articleDTO1.setCategory("자바");
    	ArticleDTO articleDTO2 = new ArticleDTO();
    	articleDTO2.setCategory("자바스크립트");
    	ArticleDTO articleDTO3 = new ArticleDTO();
    	articleDTO3.setCategory("스프링");
    	categoryService.createCategory(articleDTO1);
    	categoryService.createCategory(articleDTO2);
    	categoryService.createCategory(articleDTO3);
    	
    	
    	String[] arr = {"자바", "자바스크립트", "스프링"};
    	//article 생성
        int n = 100;
        while (n-- > 0) {
            articleRepository.save(Article.builder()
                    .title(UUID.randomUUID().toString())
                    .content(String.valueOf(100 - n))
                    .thumbnailUrl("https://picsum.photos/600/59" + (int)(Math.random() * 10))
                    .category(categoryRepository.findByTitle(arr[(int) (Math.random() * 3)]))
                    .member(memberRepository.findById(1L).get())
                    .build());
        } //카테고리별 게시글 추가
    }		

    //@Test
    public void articleTest() throws Exception {
    }
}
