package com.example.demo.websocket.redis;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.websocket.WebSocketManager;
import com.example.demo.websocket.redis.Action.Action;
import org.springframework.context.ApplicationContext;

public class DefaultRedisReceiver implements RedisReceiver {
    private ApplicationContext applicationContext;

    public DefaultRedisReceiver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public void receiveMessage(String message) {

        JSONObject object = JSONObject.parseObject(message);

        if (!object.containsKey(Action.ACTION)) {
            return;
        }
        Action action = getAction(object.getString(Action.ACTION));
        action.doMessage(getWebSocketManager(), object);
    }

    protected Action getAction(String action) {
        return getApplicationContext().getBean(action, Action.class);
    }

    protected WebSocketManager getWebSocketManager() {

        return getApplicationContext().getBean(WebSocketManager.WEBSOCKET_MANAGER_NAME, WebSocketManager.class);

    }
}
