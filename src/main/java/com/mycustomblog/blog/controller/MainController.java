package com.mycustomblog.blog.controller;

import com.mycustomblog.blog.dto.ArticleVO;
import com.mycustomblog.blog.dto.CategoryVO;
import com.mycustomblog.blog.dto.CommentForSideVO;
import com.mycustomblog.blog.service.ArticleService;
import com.mycustomblog.blog.service.CategoryService;
import com.mycustomblog.blog.service.CommentService;
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
    @Autowired
    private final CommentService commentService = null;

    //메인화면
    @GetMapping("/")
    public String index(Model model) {
        //sidebar에 뿌릴 데이터
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount();
        model.addAttribute("categoryVOs", categoryVOs);

        List<CommentForSideVO> commentVOs = commentService.recentCommentList();
        model.addAttribute("commentVOs", commentVOs);

        //메인 최신글 및 인기글
        List<ArticleVO> popularArticles = articleService.getPopularArticles();
        Slice<ArticleVO> recentArticles = articleService.getRecentArticles(0);
        model.addAttribute("popularArticles", popularArticles);
        model.addAttribute("recentArticles",recentArticles);

        return "index";
    }

    //로그인 페이지
    @GetMapping("/login")
    public String loginFrom(@RequestParam(value = "err", required = false) String err, Model model){
        if(err!=null&&err.equals("duplicatedEmail")){
            model.addAttribute("errMsg","이미 가입된 이메일입니다.");
        }

        //sidebar에 뿌릴 데이터
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount();
        model.addAttribute("categoryVOs", categoryVOs);

        List<CommentForSideVO> commentVOs = commentService.recentCommentList();
        model.addAttribute("commentVOs", commentVOs);
        return "login";
    }
}
