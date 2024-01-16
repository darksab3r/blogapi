package com.project.blogapi.comments.dto;

import lombok.Data;
import lombok.NonNull;
import org.springframework.lang.Nullable;

@Data
public class UpdateCommentDTO {

    @Nullable
    private String title;
    @Nullable
    private String body;

}
