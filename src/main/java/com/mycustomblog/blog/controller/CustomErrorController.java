package com.mycustomblog.blog.controller;

import com.mycustomblog.blog.dto.CategoryVO;
import com.mycustomblog.blog.dto.CommentForSideVO;
import com.mycustomblog.blog.service.CategoryService;
import com.mycustomblog.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {
    @Autowired
    private final CategoryService categoryService = null;
    @Autowired
    private final CommentService commentService = null;

    @GetMapping("/error")
    public String errorView(Model model){
        //sidebar에 뿌릴 데이터
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount();
        model.addAttribute("categoryVOs", categoryVOs);

        List<CommentForSideVO> commentVOs = commentService.recentCommentList();
        model.addAttribute("commentVOs", commentVOs);
        return "error/errorPage";
    }

 }
