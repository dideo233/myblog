package com.mycustomblog.blog.service;

import com.mycustomblog.blog.domain.Category;
import com.mycustomblog.blog.dto.ArticleDTO;
import com.mycustomblog.blog.dto.CategoryVO;
import com.mycustomblog.blog.repository.CategoryMapper;
import com.mycustomblog.blog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository = null;
    @Autowired
    private final CategoryMapper categoryMapper = null;
    //카테고리 검색
    public Category findCategory(String category) {
        return categoryRepository.findByTitle(category);
    }

    //카테고리 추가
    @Transactional
    public void createCategory(ArticleDTO articleDTO) {
        Category category = Category.builder().title(articleDTO.getCategory()).build();
        categoryRepository.save(category);
    }

    //카테고리 목록(+개수)
    public List<CategoryVO> getCategoryCount(){
        return categoryMapper.findCategoryInfo();
    }
}