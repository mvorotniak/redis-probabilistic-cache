package com.demo.redisprobabilisticcache.repository;

import com.demo.redisprobabilisticcache.domain.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisArticleCacheRepository {

    private static final String PREFIX = "article:";
    
    @Value("${cache.probability}")
    private double cacheProbability;

    private final RedisTemplate<String, Article> redisTemplate;


    public Optional<Article> get(Long id) {
        Optional<Article> articleOptional = Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + id));
        
        if (articleOptional.isEmpty()) {
            log.info("Article with id=[{}] does not exist in cache. ", id);
            return Optional.empty();
        } else if (isAboutToExpire(id) && shouldRefresh()) {
            log.info("Article with id=[{}] is about to expire and should refresh. " +
                    "Returning empty cache entity in order to get from database...", id);
            return Optional.empty();
        }

        return articleOptional;
    }
    
    public Set<String> getAllKeys() {
        return redisTemplate.keys("*");
    }

    private boolean isAboutToExpire(Long id) {
        return Optional.ofNullable(redisTemplate.getExpire(PREFIX + id, TimeUnit.SECONDS))
                .map(sec -> sec <= 10L)
                .orElse(false);
    }

    private boolean shouldRefresh() {
        return ThreadLocalRandom.current().nextDouble(1) < cacheProbability;
    }

    public void save(Article article) {
        log.info("Saving article=[{}] in cache...", article);
        redisTemplate.opsForValue().set(PREFIX + article.getId(), article, 1, TimeUnit.MINUTES);
    }
}
