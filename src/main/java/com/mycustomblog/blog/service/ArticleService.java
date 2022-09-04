package com.mycustomblog.blog.service;

import com.mycustomblog.blog.domain.Article;
import com.mycustomblog.blog.domain.Member;
import com.mycustomblog.blog.dto.ArticleDTO;
import com.mycustomblog.blog.dto.ArticleVO;
import com.mycustomblog.blog.repository.ArticleRepository;
import com.mycustomblog.blog.repository.CategoryRepository;
import com.mycustomblog.blog.repository.MemberRepository;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ArticleService {
    //이미지 서버로 활용하려는 git 설정값
    @Value("${git.gitToken}")
    private String gitToken;
    @Value("${git.imgRepo}")
    private String gitRepo;
    @Value("${git.imgUrl}")
    private String imgUrl;
    @Autowired
    private final MemberRepository memberRepository = null;
    @Autowired
    private final ArticleRepository articleRepository = null;
    @Autowired
    private final CategoryRepository categoryRepository = null;
    private final ModelMapper modelMapper = new ModelMapper();
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
        return Article.builder()
                .title(articleDTO.getTitle())
                .category(categoryRepository.findByTitle(articleDTO.getCategory()))
                .content(articleDTO.getContent())
                .member(member).build();
    }

    //인기글 리스트
    public List<ArticleVO> getPopularArticles() {
        List<Article> popularArticle = articleRepository.findTop6ByOrderByHitDesc();
        List<ArticleVO> articles = new ArrayList<>();

        for (Article article : popularArticle) {
            articles.add(modelMapper.map(article, ArticleVO.class)); //Article -> ArticleVO
        }
        return articles;
    }

    //최신글 리스트
    //slice : Pageable 인터페이스가 적용되는 경우의 리턴 타입
    public Slice<ArticleVO> getRecentArticles(int page) {
        return articleRepository.findByOrderByCreatedDateDesc(PageRequest.of(page, 5)).map(article -> modelMapper.map(article, ArticleVO.class));
    }

    //카테고리별 목록 (페이징)
    public Page<ArticleVO> getArticlePage(String category, Integer page){
        Page<Article> articles = articleRepository.findByCategoryOrderByCreatedDateDesc(category, PageRequest.of(pageResolver(page), 5));
        return articles.map(article -> modelMapper.map(article, ArticleVO.class));
    }

    private int pageResolver(Integer page) {
        if (page == null) {return 0;}
        else {return page-1;}
    }

    //작성글에 첨부된 이미지 git으로 업로드
    public String storeImg(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) {
            throw new IllegalArgumentException("파일이 존재하지 않습니다");
        }

        GitHub gitHub = new GitHubBuilder().withOAuthToken(gitToken).build();
        GHRepository repository = gitHub.getRepository(gitRepo);

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        repository.createContent().path("image/"+storeFileName)
                .content(multipartFile.getBytes()).message("image Upload").branch("main").commit();

        String uploadedUrl = "https://raw.githubusercontent.com/"+gitRepo +imgUrl +storeFileName;
        return uploadedUrl;
    }
    //UUID.확장자로 변경 (중복 방지)
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }
    //확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        System.out.println(pos);
        return originalFilename.substring(pos + 1);
    }
}
