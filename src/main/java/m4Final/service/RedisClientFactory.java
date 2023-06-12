package m4Final.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisClientFactory {
    private final RedisClient redisClient;

    public RedisClientFactory() {
        this.redisClient = prepareRedisClient();
    }

    private RedisClient prepareRedisClient() {
        RedisClient redisClient = RedisClient.create(RedisURI.create("localhost", 6379));
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
//            System.out.println("\nConnected to Redis\n");
        }
        return redisClient;
    }

    public RedisClient getRedisClient() {
        return redisClient;
    }
}
