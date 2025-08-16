package com.julianoclsantos.walletassignmentservice.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class RedisCacheConfigTest {

    @Test
    void shouldCreateCacheManagerWithTtlsDefined() {
        RedisConnectionFactory redisConnectionFactory = mock(RedisConnectionFactory.class);

        RedisCacheTtlProperties ttlProperties = new RedisCacheTtlProperties();
        Map<String, Long> ttlMap = new HashMap<>();
        ttlMap.put("cache1", 60L);
        ttlMap.put("cache2", 120L);
        ttlProperties.setTtl(ttlMap);

        RedisCacheConfig config = new RedisCacheConfig();
        CacheManager cacheManager = config.cacheManager(redisConnectionFactory, ttlProperties);

        cacheManager.getCache("cache1");
        cacheManager.getCache("cache2");

        assertNotNull(cacheManager, "O CacheManager não deve ser nulo");
        assertTrue(cacheManager.getCacheNames().contains("cache1"));
        assertTrue(cacheManager.getCacheNames().contains("cache2"));

        assertNotNull(cacheManager.getCache("cache1"));
        assertNotNull(cacheManager.getCache("cache2"));
    }

    @Test
    void shouldGetTtlWithSuccess() {
        RedisCacheTtlProperties properties = new RedisCacheTtlProperties();
        properties.setTtl(Map.of("test", 10L));
        assertEquals(10L, properties.getTtl().get("test").longValue());
    }

    @Test
    void shouldGetTtlWithEmpty() {
        RedisCacheTtlProperties properties = new RedisCacheTtlProperties();
        assertNull(properties.getTtl());
    }

    @Test
    void shouldGetTtlWithNull() {
        RedisCacheTtlProperties properties = new RedisCacheTtlProperties();
        properties.setTtl(null);
        assertNull(properties.getTtl());
    }

}