package quickbit.core.service.cache;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

import static quickbit.core.util.CacheConstraints.CURRENCY_PRICE_KEY;

@Service
public class CurrencyPriceCacheService {

    private final RedisTemplate<String, BigDecimal> redisTemplate;

    @Autowired
    public CurrencyPriceCacheService(
        @Qualifier("bigDecimalRedisTemplate")
        RedisTemplate<String, BigDecimal> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public BigDecimal get(@NotNull Long currencyId) {
        String key = CURRENCY_PRICE_KEY(currencyId);
        return redisTemplate.opsForValue().get(key);
    }

    public void save(@NotNull Long currencyId, @NotNull BigDecimal price) {
        String key = CURRENCY_PRICE_KEY(currencyId);
        redisTemplate.opsForValue().set(key, price, Duration.ofMinutes(30));
    }
}
