package com.adil.bridgespero.domain.repository;

import com.adil.bridgespero.config.properties.ApplicationProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class StateRedisRepository {

    private final RedissonClient redissonClient;
    private final ApplicationProperties.Redis props;

    public StateRedisRepository(RedissonClient redissonClient, ApplicationProperties properties) {
        this.redissonClient = redissonClient;
        this.props = properties.getRedis();
    }

    public void save(String name, String username) {
        saveOrUpdate(name, username);
    }

    public void update(String name, String username) {
        saveOrUpdate(name, username);
    }

    public String read(String name) {
        RBucket<String> bucket = redissonClient.getBucket(bucketName(name));
        return bucket.get();
    }

    public void delete(String name) {
        RBucket<String> bucket = redissonClient.getBucket(bucketName(name));

        if (Objects.isNull(bucket)) {
            log.warn("{} bucket not found", bucketName(name));
            return;
        }

        bucket.delete();
    }

    private void saveOrUpdate(String state, String username) {
        RBucket<String> bucket = redissonClient.getBucket(bucketName(state));
        bucket.set(username, props.getZoomStateTimeToLive(), TimeUnit.SECONDS);
    }

    private String bucketName(String name) {
        return String.join(":", props.getZoomStatePrefix(), name);
    }

}


