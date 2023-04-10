package com.example.demo.ws;


import com.example.demo.service.ShipService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/ws/connect/send/{userId}/{sign}")
@Component
@Log4j2
public class SendWebSocket {
    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象
     */
    private static final ConcurrentHashMap<String, SendWebSocket> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */

    private Session session;

    /**
     * 数据接收方用户标识
     */
    private String userId;
    /**
     * 船体标识
     */
    private String sign;

    /**
     * 数据接收服务
     */
    private static ShipService shipService;

    @Autowired
    /**
     * 数据服务注入
     */
    public void initSocket(ShipService shipService) {
        SendWebSocket.shipService = shipService;
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, @PathParam("sign") String sign) {
        this.session = session;
        this.userId = userId;
        this.sign = sign;
        webSocketMap.put(userId, this);
        addOnlineCount();           //在线数加1
        log.info("用户{}连接成功,当前在线人数为{}", userId, getOnlineCount());
        try {
            sendMessage("socket connect success");
        } catch (IOException e) {
            log.error("send message error,userId:{}", userId);
        }

    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        //从map中删除
        SendWebSocket socket = webSocketMap.get(userId);
        webSocketMap.remove(userId);
        subOnlineCount();           //在线数减1
        log.info("用户{}关闭连接！当前在线人数为{}", userId, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        log.info("来自客户端用户：{} 消息:{}", userId, message);
        if ("ping".equals(message)) {
            sendMessage("pong");
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 向客户端发送消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message) {
        for (String item : webSocketMap.keySet()) {
            try {
                webSocketMap.get(item).sendMessage(message);
            } catch (IOException e) {
            }
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        SendWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        SendWebSocket.onlineCount--;
    }

}