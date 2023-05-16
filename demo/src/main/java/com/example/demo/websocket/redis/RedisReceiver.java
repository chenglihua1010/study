package com.example.demo.websocket.redis;

public interface RedisReceiver {

    String RECEIVER_METHOD_NAME = "receiveMessage";

    String REDIS_RECEIVER_NAME = "redisReceiver";

    void receiveMessage(String message);

}
