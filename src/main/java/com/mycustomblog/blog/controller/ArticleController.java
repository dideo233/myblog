package com.mycustomblog.blog.controller;

import com.mycustomblog.blog.config.auth.PrincipalImpl;
import com.mycustomblog.blog.dto.ArticleDTO;
import com.mycustomblog.blog.dto.ArticleVO;
import com.mycustomblog.blog.dto.CategoryVO;
import com.mycustomblog.blog.dto.UploadImgDTO;
import com.mycustomblog.blog.service.ArticleService;
import com.mycustomblog.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private final ArticleService articleService = null;
    @Autowired
    private final CategoryService categoryService = null;

    //글 작성폼
    @GetMapping("article/write")
    public String writeArticleForm(ArticleDTO articleDTO, Model model){
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount(); //sidebar에 뿌릴 데이터
        model.addAttribute("categoryVOs", categoryVOs);

        return "writeForm";
    }

    //글 작성
    @PostMapping("article/write")
    public String WriteArticle(@ModelAttribute ArticleDTO articleDTO, Authentication authentication){
        PrincipalImpl principal = (PrincipalImpl) authentication.getPrincipal();
        articleDTO.setUsernum(principal.getUsernum());

        articleService.writeArticle(articleDTO);
        return "redirect:/";
    }

    //에디터의 업로드 이미지 주소 콜백용
    @PostMapping("article/uploadImg")
    @ResponseBody
    public String imgUpload(@ModelAttribute UploadImgDTO uploadImgDTO) throws IOException {
        return articleService.storeImg(uploadImgDTO.getImg());
    }

    //무한 스크롤
    @GetMapping("/main/article/{pageNum}")
    public @ResponseBody
    List<ArticleVO> nextPage(@PathVariable int pageNum){
        return articleService.getRecentArticles(pageNum).getContent();
    }
}
