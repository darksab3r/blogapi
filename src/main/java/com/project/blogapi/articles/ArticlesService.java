package com.project.blogapi.articles;

import com.project.blogapi.articles.dto.CreateArticleDTO;
import com.project.blogapi.articles.dto.UpdateArticleDTO;
import com.project.blogapi.users.UsersRepository;
import com.project.blogapi.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
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
            @Autowired ArticlesRepository articlesRepository,
            @Autowired UsersRepository usersRepository
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
        return article.orElse(null); //other way of writing is article.get();
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
        if(!isArticlePresent(articleId)){
            throw new ArticleNotFoundException(articleId);
        }
        var article = getArticleById(articleId);
        articlesRepository.delete(article);
        return !isArticlePresent(articleId);
    }

    public boolean isArticlePresent(UUID articleId){
        return articlesRepository.existsById(articleId);
    }

    public boolean likeArticle(UUID articleId, String username) {

        var article = getArticleById(articleId);
        var user = usersRepository.findByUsername(username);

        if (article.getLikes().contains(user)) {
            throw new ArticleAlreadyLikedException(username);
        }

        user.getFavouriteArticles().add(article);
        usersRepository.save(user);

        article.getLikes().add(user);
        articlesRepository.save(article);
        return true;
    }

    public boolean unlikeArticle(UUID articleId, String username) {

        var article = getArticleById(articleId);
        var user = usersRepository.findByUsername(username);

        if (!article.getLikes().contains(user)) {
            throw new ArticleNotLikedException(username);
        }

        user.getFavouriteArticles().remove(article);
        usersRepository.save(user);

        article.getLikes().remove(user);
        articlesRepository.save(article);
        return true;
    }

    static class ArticleNotFoundException extends IllegalArgumentException {
        public ArticleNotFoundException(String slug) {
            super("Article " + slug + " not found");
        }

        public ArticleNotFoundException(UUID id) {
            super("Article with id: " + id + " not found");
        }
    }

    static  class  ArticleAlreadyLikedException extends RuntimeException{
        public ArticleAlreadyLikedException(String username){
            super(username+" have already liked this article");
        }
    }

    static  class  ArticleNotLikedException extends RuntimeException{
        public ArticleNotLikedException(String username){
            super(username+" do not like this article");
        }
    }

    static class IllegalAccessException extends IllegalAccessError{
        public IllegalAccessException(){
            super("Necessary permissions to access not found");
        }
    }
}
