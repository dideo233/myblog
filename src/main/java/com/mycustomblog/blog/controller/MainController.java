package com.mycustomblog.blog.controller;

import com.mycustomblog.blog.dto.ArticleVO;
import com.mycustomblog.blog.dto.CategoryVO;
import com.mycustomblog.blog.service.ArticleService;
import com.mycustomblog.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private final ArticleService articleService = null;
    @Autowired
    private final CategoryService categoryService = null;

    @GetMapping("/")
    public String index(Model model) {
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount(); //sidebar에 뿌릴 데이터
        model.addAttribute("categoryVOs", categoryVOs);

        //메인 최신글 및 인기글
        List<ArticleVO> popularArticles = articleService.getPopularArticles();
        Slice<ArticleVO> recentArticles = articleService.getRecentArticles(0);
        model.addAttribute("popularArticles", popularArticles);
        model.addAttribute("recentArticles",recentArticles);

        return "index";
    }

    @GetMapping("/login")
    public String loginFrom(@RequestParam(value = "err", required = false) String err, Model model){
        if(err!=null&&err.equals("duplicatedEmail")){
            model.addAttribute("errMsg","이미 가입된 이메일입니다.");
        }

        List<CategoryVO> categoryVOs = categoryService.getCategoryCount(); //sidebar에 뿌릴 데이터
        model.addAttribute("categoryVOs", categoryVOs);
        return "login";
    }
}
