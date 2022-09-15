package com.mycustomblog.blog.service;

import com.mycustomblog.blog.domain.Article;
import com.mycustomblog.blog.domain.Category;
import com.mycustomblog.blog.domain.Member;
import com.mycustomblog.blog.dto.ArticleDTO;
import com.mycustomblog.blog.dto.ArticleVO;
import com.mycustomblog.blog.repository.ArticleRepository;
import com.mycustomblog.blog.repository.CategoryMapper;
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
import java.util.Objects;
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
    @Autowired
    private final CategoryMapper categoryMapper = null;
    @Autowired
    private final ModelMapper modelMapper = null;
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
                .thumbnailUrl(makeDefaultThumb(articleDTO.getThumbnailUrl()))
                .category(categoryRepository.findByTitle(articleDTO.getCategory()))
                .content(articleDTO.getContent())
                .member(member).build();
    }

    //게시글 Entity get
    public Article getArticle(Long articlenum) {
        return articleRepository.findByArticlenum(articlenum);
    }

    //디폴트 섬네일
    private String makeDefaultThumb(String thumbnailUrl) {
        String defaultThumbUrl = "https://raw.githubusercontent.com/dideo233/imageRepo/main/image/f69280db-1efc-41cf-9710-4087ed1fa573.jpg";
        if (thumbnailUrl == null || thumbnailUrl.equals("")) {
            thumbnailUrl = defaultThumbUrl;
        }
        return thumbnailUrl;
    }

    //게시글 상세보기
    public ArticleVO viewAticle(Long articlenum) {
        Article article = articleRepository.findByArticlenum(articlenum);
        return modelMapper.map(article, ArticleVO.class);
    }
    //게시글 조회수 카운트
    @Transactional
    public void addHit(Article article) {
        article.addHit();
    }

    //수정폼에서 사용할 ArticleDTO 얻기
    public ArticleDTO getArticleDTOForModify(Long articlenum) {
        Article article = articleRepository.findByArticlenum(articlenum);

        return ArticleDTO.builder()
                .category(article.getCategory().getTitle())
                .content(article.getContent())
                .thumbnaulUrl(article.getThumbnailUrl())
                .title(article.getTitle())
                .usernum(article.getMember().getUsernum()).build();
    }

    //게시글 삭제
    @Transactional
    public void deleteArticle(Long articlenum) {
        articleRepository.deleteById(articlenum);
        articleRepository.flush();
    }

    //게시글 수정
    @Transactional
    public void modifyArticle(Long articlenum, ArticleDTO articleDTO) {
        Article article = articleRepository.findByArticlenum(articlenum);

        articleDTO.setThumbnailUrl(makeDefaultThumb(articleDTO.getThumbnailUrl()));
        Category category = categoryRepository.findByTitle(articleDTO.getCategory());
        article.modifyArticle(articleDTO, category);//dirtyChecking
    }

    //게시글 0개인 카테고리 제거
    @Transactional
    public void deleteCategory(Long categorynum) {
        int count = categoryMapper.getCategoryCount(categorynum);

        if(count == 0) {
            categoryRepository.deleteById(categorynum);
        }
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
    //slice : Page와 달리 totalCount 구하는 데 쿼리 안 씀
    public Slice<ArticleVO> getRecentArticles(int page) {
        return articleRepository.findByOrderByCreatedDateDesc(PageRequest.of(page, 5)).map(article -> modelMapper.map(article, ArticleVO.class));
    }

    //카테고리별 목록 (페이징)
    public Page<ArticleVO> getArticlePage(String category, Integer page){
        Page<Article> articles = articleRepository.findByCategoryOrderByCreatedDateDesc(category, PageRequest.of(pageResolver(page), 5));
        return articles.map(article -> modelMapper.map(article, ArticleVO.class));
    }
    private int pageResolver(Integer page) { //디폴트 페이지 번호
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
        String uploadedUrl = imgUrl +storeFileName;
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
