package com.project.blogapi.articles.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Data
@Setter
public class CreateArticleDTO {

    @NonNull
    private String title;
    @NonNull
    private String body;
    @Nullable
    private String subtitle;
}
