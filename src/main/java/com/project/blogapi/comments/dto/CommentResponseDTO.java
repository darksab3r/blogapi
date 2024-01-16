package com.project.blogapi.comments.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CommentResponseDTO {

    @NonNull
    String id;

    @NonNull
    String title;

    @NonNull
    String body;

    @NonNull
    String author;
}
