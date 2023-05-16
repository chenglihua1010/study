package com.example.demo.websocket.redis.Action;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.websocket.WebSocket;
import com.example.demo.websocket.WebSocketManager;
import com.example.demo.websocket.util.WebSocketUtil;

public class SendMessageAction implements Action{
    @Override
    public void doMessage(WebSocketManager manager, JSONObject object) {
        if(!object.containsKey(IDENTIFIER)){
            return;
        }
        if(!object.containsKey(MESSAGE)){
            return;
        }

        String identifier = object.getString(IDENTIFIER);

        WebSocket webSocket = manager.get(identifier);
        if(null == webSocket){
            return;
        }
        WebSocketUtil.sendMessage(webSocket.getSession() , object.getString(MESSAGE));
    }
}
