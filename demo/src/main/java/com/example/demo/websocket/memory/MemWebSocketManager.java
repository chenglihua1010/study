package com.example.demo.websocket.memory;

import com.example.demo.websocket.WebSocket;
import com.example.demo.websocket.WebSocketManager;
import com.example.demo.websocket.util.WebSocketUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemWebSocketManager implements WebSocketManager, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private final Map<String, WebSocket> connections = new ConcurrentHashMap<>(100);


    @Override
    public WebSocket get(String identifier) {
        return connections.get(identifier);
    }

    @Override
    public void put(String identifier, WebSocket webSocket) {
        connections.put(identifier, webSocket);
        //连接事件
//        getApplicationContext().publishEvent(new ApplicationEvent());

    }

    @Override
    public void remove(String identifier) {
        WebSocket webSocket = connections.remove(identifier);
        if (webSocket != null) {
            //关闭事件
//        getApplicationContext().publishEvent(new ApplicationEvent());

        }

    }

    @Override
    public Map<String, WebSocket> localWebSocketMap() {
        return connections;
    }

    @Override
    public void sendMessage(String identifier, String message) {

        WebSocket webSocket = get(identifier);
        if (webSocket != null) {
            WebSocketUtil.sendMessage(webSocket.getSession(), message);
        }
    }

    @Override
    public void onMessage(String identifier, String message) {

    }

    @Override
    public void broadcast(String message) {

    }


}
