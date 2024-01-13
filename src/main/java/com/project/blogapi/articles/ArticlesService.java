package com.project.blogapi.articles;

import com.project.blogapi.articles.dto.CreateArticleDTO;
import com.project.blogapi.users.UsersRepository;
import org.bouncycastle.util.io.Streams;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ArticlesService {

    public final ArticlesRepository articlesRepository;
    public final UsersRepository usersRepository;

    public ArticlesService(
            ArticlesRepository articlesRepository,
            UsersRepository usersRepository
    ) {
        this.articlesRepository = articlesRepository;
        this.usersRepository = usersRepository;
    }

    public List<ArticleEntity> getAllArticles(){
        List<ArticleEntity> articlesIterable = articlesRepository.findAll();
        return StreamSupport.stream(articlesIterable.spliterator(),false)
                .collect(Collectors.toList());
    }

    public ArticleEntity getArticleBySlug(String slug){
        var article = articlesRepository.findBySlug(slug);
        if(slug==null){
            return null;
        }
        return article;
    }

    public ArticleEntity createArticle(CreateArticleDTO createArticleDTO, String authorUsername){
        var author = usersRepository.findByUsername(authorUsername);
        return articlesRepository.save(
                ArticleEntity.builder()
                        .title(createArticleDTO.getTitle())
                        .slug(createArticleDTO.getTitle().toLowerCase().replaceAll(" ","-"))
                        .subtitle(createArticleDTO.getSubtitle())
                        .body(createArticleDTO.getBody())
                        .author(author)
                        .build()
                );

    }
}
