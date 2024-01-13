package com.project.blogapi.users.dto;

import lombok.Data;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

@Data
public class CreateUserRequestDTO {

    @NotNull
    String username;
    @NonNull
    String email;
    @NonNull
    String password;
}
