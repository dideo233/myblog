package com.mycustomblog.blog.controller;

import com.mycustomblog.blog.config.auth.PrincipalImpl;
import com.mycustomblog.blog.domain.Article;
import com.mycustomblog.blog.dto.ArticleDTO;
import com.mycustomblog.blog.dto.ArticleVO;
import com.mycustomblog.blog.dto.CategoryVO;
import com.mycustomblog.blog.dto.UploadImgDTO;
import com.mycustomblog.blog.service.ArticleService;
import com.mycustomblog.blog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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

        return "article/writeForm";
    }

    //글 작성
    @PostMapping("article/write")
    public String WriteArticle(@ModelAttribute ArticleDTO articleDTO, Authentication authentication){
        PrincipalImpl principal = (PrincipalImpl) authentication.getPrincipal();
        articleDTO.setUsernum(principal.getUsernum());

        if(categoryService.findCategory(articleDTO.getCategory()) == null) {//카테고리 유무 판단
            categoryService.createCategory(articleDTO);
        }

        articleService.writeArticle(articleDTO);
        return "redirect:/";
    }

    //게시글 상세보기
    @GetMapping("article/view")
    public String viewArticle(@RequestParam Long articlenum, Authentication authentication,
                              @CookieValue(required = false, name = "view") String cookie, HttpServletResponse response,
                              Model model) {
        if(authentication != null) {
            PrincipalImpl principal = (PrincipalImpl) authentication.getPrincipal();
            model.addAttribute("picUrl", principal.getMemberPicUrl());
        } //댓글 작성 회원의 프로필 이미지용
        
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount(); //sidebar에 뿌릴 데이터
        model.addAttribute("categoryVOs", categoryVOs);

        ArticleVO article = articleService.viewAticle(articlenum);
        model.addAttribute("article", article);

        //조회수 카운트 - 브라우저마다 읽은 게시글 정보를 유지. 같은 브라우저에서의 연속적인 조회 카운트를 방지
        addHitWithCookie(articleService.getArticle(articlenum), cookie, response);
        return "article/viewArticle";
    }

    //게시글 수정 화면으로 이동
    @GetMapping("article/modify")
    public String modifyArticleForm(@RequestParam Long articlenum, Model model) {
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount(); //sidebar에 뿌릴 데이터
        model.addAttribute("categoryVOs", categoryVOs);

        ArticleDTO articleDto = articleService.getArticleDTOForModify(articlenum);
        model.addAttribute("articleDto", articleDto);
        model.addAttribute("articlenum", articlenum);
        return "article/modifyArticleForm";
    }

    //게시글 수정
    @PostMapping("article/modify")
    public String modifyArticle(@RequestParam Long articlenum,
                                @ModelAttribute ArticleDTO articleDTO, Authentication authentication) {
        if(categoryService.findCategory(articleDTO.getCategory()) == null) {//카테고리 유무 판단
            categoryService.createCategory(articleDTO);
        }
        PrincipalImpl principal = (PrincipalImpl) authentication.getPrincipal();
        articleDTO.setUsernum(principal.getUsernum());
        articleService.modifyArticle(articlenum, articleDTO);
        return "redirect:/";
    }

    //게시글 삭제
    @PostMapping("article/delete")
    public String deleteArticle(@RequestParam Long articlenum) {
        articleService.deleteArticle(articlenum);
        return "redirect:/";
    }

    //카테고리별 목록
    @GetMapping("article/list")
    public String getArticlesList(@RequestParam String category,
                                  @RequestParam(required = false) Integer page, Model model) {
        List<CategoryVO> categoryVOs = categoryService.getCategoryCount(); //sidebar에 뿌릴 데이터
        model.addAttribute("categoryVOs", categoryVOs);

        Page<ArticleVO> articles = articleService.getArticlePage(category, page);
        model.addAttribute("articles", articles);
        return "article/listByCategory";
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

    //조회한 게시글 정보 쿠키
    private void addHitWithCookie(Article article, String cookie, HttpServletResponse response) {
        Long articlenum = article.getArticlenum();
        if (cookie == null) {
            Cookie viewCookie = new Cookie("view", articlenum + "/");
            viewCookie.setComment("게시물 조회 중복 체크용 쿠키");
            viewCookie.setMaxAge(60 * 60);
            articleService.addHit(article);
            response.addCookie(viewCookie);
        } else {
            boolean isRead = false;
            String[] viewCookieList = cookie.split("/");
            for (String alreadyRead : viewCookieList) {
                if (alreadyRead.equals(String.valueOf(articlenum))) {
                    isRead = true;
                    break;
                }
            }
            if (!isRead) {
                cookie += articlenum + "/";
                articleService.addHit(article);
            }
            response.addCookie(new Cookie("view", cookie)); //@CookeyValue로 읽을 수 있는 듯
        }
    }
}
