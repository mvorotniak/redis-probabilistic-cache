package com.demo.redisprobabilisticcache.controller;

import java.util.List;

import com.demo.redisprobabilisticcache.domain.Article;
import com.demo.redisprobabilisticcache.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ArticleController {

  private final CacheService cacheService;

  @GetMapping("/article")
  public ResponseEntity<Article> getArticle(@RequestParam Long id) {
    Article article = cacheService.getArticle(id);

    return ResponseEntity.ok(article);
  }

  @GetMapping("/populate-cache")
  public ResponseEntity<String> populateCache() {
    cacheService.multiSet();
    return ResponseEntity.ok("OK");
  }

  @GetMapping("/get-cached-keys")
  public ResponseEntity<List<String>> getAllCachedKeys() {
    return ResponseEntity.ok(cacheService.getAllKeys());
  }
}
