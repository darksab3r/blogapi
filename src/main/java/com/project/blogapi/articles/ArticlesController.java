package com.project.blogapi.articles;

import com.project.blogapi.articles.dto.ArticleResponseDTO;
import com.project.blogapi.articles.dto.CreateArticleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/articles")
public class ArticlesController {

    private final ArticlesService articlesService;
    private final ModelMapper modelMapper;

    public ArticlesController(
            @Autowired ArticlesService articlesService,
            @Autowired ModelMapper modelMapper
    ) {
        this.articlesService = articlesService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    ResponseEntity<List<ArticleResponseDTO>> getAllArticles(){
        List<ArticleEntity> articles = articlesService.getAllArticles();

        List<ArticleResponseDTO> response = articles.stream()
                .map(articleEntity -> modelMapper.map(articleEntity, ArticleResponseDTO.class))
                .toList();

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<Void> getArticleByID(){
        return null;
    }

    @PostMapping("")
    ResponseEntity<ArticleResponseDTO> createArticle(
            @RequestBody CreateArticleDTO createArticleDTO,
            @AuthenticationPrincipal String username
    ){
        var article = articlesService.createArticle(createArticleDTO,username);
        var response = modelMapper.map(article,ArticleResponseDTO.class);
        return ResponseEntity.accepted().body(response);
    }

    @PatchMapping("/{id}")
    ResponseEntity<Void> updateArticle(){
        return null;
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteArticle(){

        return null;
    }

    @PutMapping("/{id}/like") //idempotent
    ResponseEntity<Void> likeArticle(){

        return null;
    }
    @DeleteMapping
    ResponseEntity<Void> unlikeArticle(){

        return null;
    }
}
