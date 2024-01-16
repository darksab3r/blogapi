package com.project.blogapi.users.dto;

import com.project.blogapi.users.UserEntity;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    @NonNull
    String username;
    @NonNull
    String email;
    @Nullable
    String bio;

    @Nullable
    List<String> followers;

    @Nullable
    List<String> following;

    String token;
}