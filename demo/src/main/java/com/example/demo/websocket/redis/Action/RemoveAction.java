package com.example.demo.websocket.redis.Action;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.websocket.WebSocket;
import com.example.demo.websocket.WebSocketManager;

import java.util.Map;

public class RemoveAction implements Action{
    @Override
    public void doMessage(WebSocketManager manager, JSONObject object) {
        if(!object.containsKey(IDENTIFIER)){
            return;
        }

        String identifier = object.getString(IDENTIFIER);

        Map<String, WebSocket> localWebSocketMap = manager.localWebSocketMap();
        if(localWebSocketMap.containsKey(identifier)){
            localWebSocketMap.remove(identifier);
        }
    }
}
