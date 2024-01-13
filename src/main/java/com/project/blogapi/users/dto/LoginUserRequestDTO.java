package com.project.blogapi.users.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class LoginUserRequestDTO {
    @NonNull
    String username;

    @NonNull
    String password;
}
