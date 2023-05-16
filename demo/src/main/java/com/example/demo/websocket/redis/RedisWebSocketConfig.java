package com.example.demo.websocket.redis;

import com.example.demo.websocket.util.SpringContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

public class RedisWebSocketConfig implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }


    @Bean(RedisWebSocketManager.WEBSOCKET_MANAGER_NAME)
    @ConditionalOnMissingBean(name = RedisWebSocketManager.WEBSOCKET_MANAGER_NAME)
    public RedisWebSocketManager websocketManager(@Autowired StringRedisTemplate stringRedisTemplate) {
        return new RedisWebSocketManager(stringRedisTemplate);

    }


    @Bean(RedisReceiver.REDIS_RECEIVER_NAME)
    public RedisReceiver redisReceiver() {
        return new DefaultRedisReceiver(getApplicationContext());
    }

    @Bean
    public MessageListener listenerAdapter(@Qualifier(RedisReceiver.REDIS_RECEIVER_NAME) RedisReceiver redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, RedisWebSocketManager.WEBSOCKET_MANAGER_NAME);
    }


    @Bean("redisMessageListenerContainer")
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                                       MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(RedisWebSocketManager.CHANNEL));
        return container;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringContextHolder springContextHolder(){
        return new SpringContextHolder();
    }

}
