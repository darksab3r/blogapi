package com.project.blogapi.articles;

import com.project.blogapi.articles.dto.CreateArticleDTO;
import com.project.blogapi.articles.dto.UpdateArticleDTO;
import com.project.blogapi.users.UsersRepository;
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
        if(slug==null){
            return null;
        }
        return articlesRepository.findBySlug(slug);
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

    public ArticleEntity getArticleById(UUID id){
        var article = articlesRepository.findById(id);
        return article.orElse(null);
    }

    public ArticleEntity updateArticle(UUID articleId, UpdateArticleDTO updateArticleDTO){

        var article = articlesRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException(articleId));

        if(updateArticleDTO.getTitle()!=null){
            article.setTitle(updateArticleDTO.getTitle());
            article.setSlug(updateArticleDTO.getTitle().toLowerCase().replaceAll(" ","-"));
        }

        if(updateArticleDTO.getBody()!=null){
            article.setBody(updateArticleDTO.getBody());
        }

        if(updateArticleDTO.getSubtitle()!=null){
            article.setBody(updateArticleDTO.getBody());
        }

        return articlesRepository.save(article);
    }

    public void verifyAuthor(UUID articleId, String username){
        if(getArticleById(articleId).getAuthor()!=usersRepository.findByUsername(username)){
            throw new IllegalAccessException();
        }
    }

    public boolean deleteArticle(UUID articleId){
        if(isArticlePresent(articleId)){
            throw new ArticleNotFoundException("Article not found");
        }
        var article = getArticleById(articleId);
        articlesRepository.delete(article);
        return isArticlePresent(articleId);
    }

    public boolean isArticlePresent(UUID articleId){
        return !articlesRepository.existsById(articleId);
    }
    static class ArticleNotFoundException extends IllegalArgumentException {
        public ArticleNotFoundException(String slug) {
            super("Article " + slug + " not found");
        }

        public ArticleNotFoundException(UUID id) {
            super("Article with id: " + id + " not found");
        }
    }

    static class IllegalAccessException extends IllegalAccessError{
        public IllegalAccessException(){
            super("Necessary permissions to access not found");
        }
    }
}
