package com.project.blogapi.articles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArticlesRepository extends JpaRepository<ArticleEntity, UUID> {
    ArticleEntity findBySlug(String slug);
}
