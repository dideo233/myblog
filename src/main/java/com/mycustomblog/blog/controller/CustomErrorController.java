package com.mycustomblog.blog.controller;

import com.mycustomblog.blog.dto.CategoryVO;
import com.mycustomblog.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CustomErrorController implements ErrorController {
    @Autowired
    private final CategoryService categoryService = null;

    @GetMapping("/error")
    public String errorView(Model model, HttpServletRequest request){
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount(); //sidebar에 뿌릴 데이터
        model.addAttribute("categoryVOs", categoryVOs);
        return "/error/errorPage";
    }
 }
