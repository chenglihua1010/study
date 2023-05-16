package com.example.demo.websocket.redis.Action;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.websocket.WebSocketManager;

public interface Action {


    String IDENTIFIER = "identify";

    String MESSAGE = "message";

    String ACTION = "action";

    void doMessage(WebSocketManager webSocketManager, JSONObject jsonObject);
}
