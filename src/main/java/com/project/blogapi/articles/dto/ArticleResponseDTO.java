package com.project.blogapi.articles.dto;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDTO {

    @NonNull
    private String articleId;

    @NonNull
    private String title;

    @NonNull
    private String body;

    @NonNull
    private String author;

    @NonNull
    private String slug;
}
