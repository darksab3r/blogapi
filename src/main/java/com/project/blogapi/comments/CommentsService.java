package com.project.blogapi.comments;

import com.project.blogapi.articles.ArticlesService;
import com.project.blogapi.articles.dto.UpdateArticleDTO;
import com.project.blogapi.comments.dto.CreateCommentDTO;
import com.project.blogapi.comments.dto.UpdateCommentDTO;
import com.project.blogapi.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CommentsService {

    public final CommentsRepository commentsRepository;
    public final ArticlesService articlesService;
    public final UsersService usersService;


    public CommentsService(
            @Autowired CommentsRepository commentsRepository,
            @Autowired ArticlesService articlesService,
            @Autowired UsersService usersService
    ) {
        this.commentsRepository = commentsRepository;
        this.articlesService = articlesService;
        this.usersService = usersService;
    }

    public CommentEntity createComment(CreateCommentDTO createCommentDTO, UUID articleId, String authorUsername){
        var article = articlesService.getArticleById(articleId);
        var author = usersService.getUserByUsername(authorUsername);

        return commentsRepository.save(
                CommentEntity.builder()
                        .article(article)
                        .author(author)
                        .title(createCommentDTO.getTitle())
                        .body(createCommentDTO.getBody())
                        .build()
        );
    }

    public List<CommentEntity> getAllCommentsOnArticle(UUID articleId){
        var article = articlesService.getArticleById(articleId);
        List<CommentEntity> commentsIterable = commentsRepository.findAllByArticle(article);
        return StreamSupport.stream(commentsIterable.spliterator(),false)
                .collect(Collectors.toList());
    }

    public CommentEntity getCommentById(UUID commentId){
        var comment = commentsRepository.findById(commentId);
        return comment.orElse(null);
    }

    public boolean verifyAuthor(
            UUID commentId,
            String username
    ){
        if(getCommentById(commentId).getAuthor()!=usersService.getUserByUsername(username)){
            throw new IllegalAccessException();
        }
        return true;
    }

    public CommentEntity updateComment(UUID commentId, UpdateCommentDTO updateCommentDTO){
        var comment = commentsRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));

        if(updateCommentDTO.getTitle()!=null){
            comment.setTitle(updateCommentDTO.getTitle());
        }

        if(updateCommentDTO.getBody()!=null){
            comment.setBody(updateCommentDTO.getBody());
        }

        return commentsRepository.save(comment);
    }

    public boolean isCommentPresent(UUID commentId){
        return commentsRepository.existsById(commentId);
    }

    public boolean deleteComment(UUID commentId){
        if(!isCommentPresent(commentId)){
            throw new CommentNotFoundException(commentId);
        }
        var comment = getCommentById(commentId);
        commentsRepository.delete(comment);
        return !isCommentPresent(commentId);
    }

    static class IllegalAccessException extends IllegalAccessError{
        public IllegalAccessException(){
            super("Necessary permissions to access not found");
        }
    }
    static class CommentNotFoundException extends IllegalArgumentException {
        public CommentNotFoundException(UUID id) {
            super("Comment with id: " + id + " not found");
        }
    }
}
