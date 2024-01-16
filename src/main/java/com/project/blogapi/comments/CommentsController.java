package com.project.blogapi.comments;

import com.project.blogapi.articles.dto.ArticleResponseDTO;
import com.project.blogapi.articles.dto.CreateArticleDTO;
import com.project.blogapi.comments.dto.CommentResponseDTO;
import com.project.blogapi.comments.dto.CreateCommentDTO;
import com.project.blogapi.comments.dto.UpdateCommentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentsController {

    public final CommentsService commentsService;
    private final ModelMapper modelMapper;

    public CommentsController(
            @Autowired CommentsService commentsService,
            @Autowired ModelMapper modelMapper
    ) {
        this.commentsService = commentsService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    ResponseEntity<CommentResponseDTO> createComment(
            @PathVariable UUID articleId,
            @AuthenticationPrincipal String username,
            @RequestBody CreateCommentDTO createCommentDTO
            ){
        var comment = commentsService.createComment(createCommentDTO,articleId,username);
        var response = modelMapper.map(comment, CommentResponseDTO.class);
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("")
    ResponseEntity<List<CommentResponseDTO>> getAllCommentsOnArticle(
            @PathVariable UUID articleId,
            @AuthenticationPrincipal String username
    ){
        List<CommentEntity> comments = commentsService.getAllCommentsOnArticle(articleId);
        List<CommentResponseDTO> response = comments.stream()
                .map(commentEntity -> modelMapper.map(commentEntity, CommentResponseDTO.class))
                .toList();

        return ResponseEntity.accepted().body(response);
    }

    @PatchMapping("/{commentId}")
    ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable UUID commentId,
            @RequestBody UpdateCommentDTO updateCommentDTO,
            @AuthenticationPrincipal String username
    ){

        commentsService.verifyAuthor(commentId,username);
        var updatedComment = commentsService.updateComment(commentId,updateCommentDTO);
        var response = modelMapper.map(updatedComment, CommentResponseDTO.class);
        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping("/{commentId}")
    ResponseEntity<String> deleteComment(
            @PathVariable UUID commentId,
            @AuthenticationPrincipal String username
    ){
        commentsService.verifyAuthor(commentId,username);
        commentsService.deleteComment(commentId);
        return ResponseEntity.accepted().body("Comment deleted");
    }
}
