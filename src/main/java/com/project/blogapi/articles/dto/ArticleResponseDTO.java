package com.project.blogapi.articles.dto;

import lombok.*;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDTO {

    @NonNull
    private String title;

    @NonNull
    private String body;

    @NonNull
    private String author;
}
