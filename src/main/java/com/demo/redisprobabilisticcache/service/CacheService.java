package com.demo.redisprobabilisticcache.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.demo.redisprobabilisticcache.domain.Article;
import com.demo.redisprobabilisticcache.repository.ArticleRepository;
import com.demo.redisprobabilisticcache.repository.RedisArticleCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

  private final ArticleRepository articleRepository;

  private final RedisArticleCacheRepository redisArticleCacheRepository;

  public Article getArticle(Long id) {
    Optional<Article> articleOptional = redisArticleCacheRepository.get(id);

    log.info("Article with id=[{}] found in cache=[{}]", id, articleOptional.isPresent());

    return articleOptional
        .orElseGet(() -> articleRepository.findById(id)
            .map(a -> {
              redisArticleCacheRepository.save(a);
              return a;
            })
            .orElse(null));
  }

  public void multiSet() {
    IntStream.range(0, 10_000).forEach(i -> {
      Article article = new Article();
      article.setId((long) i);
      article.setName("random article-%s".formatted(i));

      log.info("Saving article [{}]", article.getName());
      redisArticleCacheRepository.save(article);
    });
  }

  public List<String> getAllKeys() {
    List<String> keys = new ArrayList<>(redisArticleCacheRepository.getAllKeys());
    sort(keys);
    log.info("Articles cache has [{}] cached entities", keys.size());

    return keys;
  }

  private void sort(List<String> keys) {
    keys.sort((o1, o2) -> {
      int num1 = Integer.parseInt(o1.split(":")[1]);
      int num2 = Integer.parseInt(o2.split(":")[1]);
      return Integer.compare(num1, num2);
    });

  }
}
