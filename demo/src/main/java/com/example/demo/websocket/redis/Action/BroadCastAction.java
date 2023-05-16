package com.example.demo.websocket.redis.Action;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.websocket.WebSocketManager;
import com.example.demo.websocket.util.WebSocketUtil;

public class BroadCastAction implements Action {
    @Override
    public void doMessage(WebSocketManager manager, JSONObject object) {
        if (!object.containsKey(MESSAGE)) {
            return;
        }
        String message = object.toJSONString(MESSAGE);
        //从本地取出所有的websocket发送消息
        manager.localWebSocketMap().values().forEach(
                webSocket -> WebSocketUtil.sendMessage(
                        webSocket.getSession(), message));
    }
}
