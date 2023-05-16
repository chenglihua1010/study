package com.example.demo.websocket;

import com.example.demo.websocket.util.SpringContextHolder;

import javax.websocket.Session;
import java.util.Date;

public abstract class BaseWebSocketEndPoint {

    public void connect(String identifier, Session session) {
        WebSocket webSocket = new WebSocket().setSession(session).setIdentifier(identifier).setLastDate(new Date());
        getWebSocketManager().put(identifier, webSocket);

    }

    public void disconnect() {

    }

    public void receiveMessage(String message) {
        //心跳检测


    }

    protected WebSocketManager getWebSocketManager() {
        return SpringContextHolder.getBean(WebSocketManager.WEBSOCKET_MANAGER_NAME, WebSocketManager.class);
    }

}
