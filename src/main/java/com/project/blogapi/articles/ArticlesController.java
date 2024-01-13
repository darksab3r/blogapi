package com.project.blogapi.articles;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticlesController {

    @GetMapping("")
    ResponseEntity<String> getAllArticles(){
        return ResponseEntity.ok("All articles");
    }

    @GetMapping("/{id}")
    ResponseEntity<Void> getArticleByID(){
        return null;
    }

    @PostMapping("")
    ResponseEntity<String> createArticle(
            @AuthenticationPrincipal String username
    ){
        return ResponseEntity.accepted().body("Article Created by "+username);
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
