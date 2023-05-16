package com.example.demo.websocket.redis;

import com.example.demo.websocket.WebSocket;
import com.example.demo.websocket.memory.MemWebSocketManager;
import com.example.demo.websocket.redis.Action.Action;
import com.example.demo.websocket.redis.Action.BroadCastAction;
import com.example.demo.websocket.redis.Action.RemoveAction;
import com.example.demo.websocket.redis.Action.SendMessageAction;
import com.example.demo.websocket.util.JsonUtil;
import com.example.demo.websocket.util.WebSocketUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;

public class RedisWebSocketManager extends MemWebSocketManager {

    public static final String CHANNEL = "websocket";
    public static final String COUNT_KEY = "RedisWebSocketManagerCountKey";

    protected StringRedisTemplate redisTemplate;


    public RedisWebSocketManager(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void put(String identifier, WebSocket webSocket) {
        super.put(identifier, webSocket);
        //在线数量加1
        countChange(1);
    }

    @Override
    public void remove(String identifier) {
        boolean containsKey = localWebSocketMap().containsKey(identifier);
        if (containsKey) {
            super.remove(identifier);
        } else {
            Map<String, Object> map = new HashMap<>(2);
            map.put(Action.ACTION, RemoveAction.class.getName());
            map.put(Action.IDENTIFIER, identifier);
            //在websocket频道上发布发送消息的消息
            redisTemplate.convertAndSend(getChannel(), JsonUtil.serializeMap(map));
        }
        //在线数量减1
        countChange(-1);
    }

    @Override
    public int size() {
        return getCount();
    }

    @Override
    public void sendMessage(String identifier, String message) {
        WebSocket webSocket = get(identifier);
        //本地能找到就直接发
        if (null != webSocket) {
            WebSocketUtil.sendMessage(webSocket.getSession(), message);
            return;
        }

        Map<String, Object> map = new HashMap<>(3);
        map.put(Action.ACTION, SendMessageAction.class.getName());
        map.put(Action.IDENTIFIER, identifier);
        map.put(Action.MESSAGE, message);
        //在websocket频道上发布发送消息的消息
        redisTemplate.convertAndSend(getChannel(), JsonUtil.serializeMap(map));
    }

    @Override
    public void broadcast(String message) {
        Map<String, Object> map = new HashMap<>(2);
        map.put(Action.ACTION, BroadCastAction.class.getName());
        map.put(Action.MESSAGE, message);
        //在websocket频道上发布广播的消息
        redisTemplate.convertAndSend(getChannel(), JsonUtil.serializeMap(map));
    }

    protected String getChannel() {
        return CHANNEL;
    }

    private void countChange(int delta) {
        ValueOperations<String, String> value = redisTemplate.opsForValue();

        //获取在线当前数量
        int count = getCount(value);

        count = count + delta;
        count = count > 0 ? count : 0;

        //设置新的数量
        value.set(COUNT_KEY, "" + count);
    }

    /**
     * 获取当前在线数量
     */
    private int getCount() {
        ValueOperations<String, String> value = redisTemplate.opsForValue();
        return getCount(value);
    }

    private int getCount(ValueOperations<String, String> value) {
        String countStr = value.get(COUNT_KEY);
        int count = 0;
        if (null != countStr) {
            count = Integer.parseInt(countStr);
        }
        return count;
    }


}
