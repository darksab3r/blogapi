package com.project.blogapi.articles;

import com.project.blogapi.articles.dto.ArticleResponseDTO;
import com.project.blogapi.articles.dto.CreateArticleDTO;
import com.project.blogapi.articles.dto.UpdateArticleDTO;
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
    ResponseEntity<ArticleResponseDTO>getArticleById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.accepted().body(modelMapper.map(articlesService.getArticleById(id), ArticleResponseDTO.class));
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
    ResponseEntity<ArticleResponseDTO> updateArticle(
            @PathVariable UUID id,
            @AuthenticationPrincipal String username,
            @RequestBody UpdateArticleDTO updateArticleDTO
            ){
        articlesService.verifyAuthor(id,username);
        var updatedArticle = articlesService.updateArticle(id,updateArticleDTO);
        var response = modelMapper.map(updatedArticle,ArticleResponseDTO.class);
        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteArticle(
            @PathVariable UUID id,
            @AuthenticationPrincipal String username
    ){
        articlesService.verifyAuthor(id,username);
        articlesService.deleteArticle(id);
        return ResponseEntity.accepted().body("Article deleted");
    }

    @PutMapping("/{id}/like") //idempotent
    ResponseEntity<String> likeArticle(
            @PathVariable UUID id,
            @AuthenticationPrincipal String username
    ){
        articlesService.likeArticle(id,username);
        return ResponseEntity.accepted().body(username+" liked article "+getArticleById(id).getBody().getTitle());
    }
    @DeleteMapping("/{id}/unlike")
    ResponseEntity<String> unlikeArticle(
            @PathVariable UUID id,
            @AuthenticationPrincipal String username
    ){
        articlesService.unlikeArticle(id,username);
        return ResponseEntity.accepted().body(username+" unliked article "+getArticleById(id).getBody().getTitle());
    }
}
