package com.websocket;

import com.utils.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/{sid}")
public class WebSocket {
    private static int onlineCount = 0;
    private static final ConcurrentHashMap<String, WebSocket> webSocketMap = new ConcurrentHashMap<>();
    private Session session;
    private String sid;

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        this.sid = sid;
        webSocketMap.put(sid, this);
        addOnlineCount();
    }

    @OnClose
    public void onClose() {
        webSocketMap.remove(this.sid);
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String message) {
        if (StringUtils.isEmpty(message)) {
            for (WebSocket socket : webSocketMap.values()) {
                try {
                    socket.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }
}
