package quickbit.core.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import quickbit.core.model.NewsModel;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class NewsCacheService {

    private static final String NEWS_CACHE_KEY = "#news.feed";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public NewsCacheService(
        @Qualifier("stringRedisTemplate")
        RedisTemplate<String, String> redisTemplate,
        ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public List<NewsModel> getNews() {
        try {
            String string = redisTemplate.opsForValue().get(NEWS_CACHE_KEY);
            if (Objects.nonNull(string)) {
                return objectMapper.readValue(string, new TypeReference<>() {});
            }
            return Collections.emptyList();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveNews(@NotNull List<NewsModel> news) {
        try {
            String jsonString = objectMapper.writeValueAsString(news);
            redisTemplate.opsForValue().set(NEWS_CACHE_KEY, jsonString, Duration.ofHours(5));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearNewsCache() {
        redisTemplate.delete(NEWS_CACHE_KEY);
    }
}
