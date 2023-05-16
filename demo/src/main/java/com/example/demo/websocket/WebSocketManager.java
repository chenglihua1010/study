package com.example.demo.websocket;

import java.util.Map;

public interface WebSocketManager {
    String WEBSOCKET_MANAGER_NAME = "webSocketManager";

    WebSocket get(String identifier);

    void put(String identifier, WebSocket webSocket);

    void remove(String identifier);

    Map<String, WebSocket> localWebSocketMap();

    void sendMessage(String identifier, String message);

    void onMessage(String identifier, String message);

    default int size(){
        return localWebSocketMap().size();
    }

    /**
     * 广播
     * @param message 消息
     */
    void broadcast(String message);

}
