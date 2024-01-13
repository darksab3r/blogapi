package com.project.blogapi.security.tokens;

import com.project.blogapi.common.BaseEntity;
import com.project.blogapi.users.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity(name="user_tokens")
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserTokenEntity extends BaseEntity {

    @ManyToOne
    UserEntity user;

    Date expiresAt;
}
