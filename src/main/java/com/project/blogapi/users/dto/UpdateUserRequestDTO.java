package com.project.blogapi.users.dto;

import lombok.Data;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.lang.Nullable;

@Data
public class UpdateUserRequestDTO {

    @Nullable
    String username;
    @Nullable
    String email;
    @Nullable
    String password;
    @Nullable
    String bio;
}
