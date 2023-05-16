package com.example.demo;

import com.example.demo.websocket.memory.EnableMemWebSocketManager;
import com.example.demo.websocket.redis.EnableRedisWebSocketManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
//@EnableMemWebSocketManager
@EnableRedisWebSocketManager
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
