package com.example.demo.websocket;


import lombok.Data;
import lombok.experimental.Accessors;

import javax.websocket.Session;
import java.util.Date;

@Data
@Accessors(chain = true)
public class WebSocket {

    private Session session;
    private String identifier;
    private Date lastDate;
}
