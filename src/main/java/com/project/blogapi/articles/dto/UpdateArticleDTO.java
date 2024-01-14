package com.project.blogapi.articles.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Data
@Setter(AccessLevel.NONE)
public class UpdateArticleDTO {
    @Nullable
    private String title;
    @Nullable
    private String body;
    @Nullable
    private String subtitle;
}
