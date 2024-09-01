package com.demo.redisprobabilisticcache.repository;

import com.demo.redisprobabilisticcache.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
