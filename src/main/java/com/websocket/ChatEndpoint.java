package com.websocket;

import com.utils.MessageUtils;
import com.utils.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/chat")
public class ChatEndpoint {
    private static int onlineCount = 0;
    private static final ConcurrentHashMap<String, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();
    private Session session;
    private HttpSession httpSession;
    private String sid;

    private Set<String> getNames() {
        return onlineUsers.keySet();
    }

    private void broadcastAllUsers(String message) {
        try {
            Set<String> names = onlineUsers.keySet();
            for (String name : names) {
                ChatEndpoint chatEndpoint = onlineUsers.get(name);
                chatEndpoint.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.sid = sid;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String res_username = (String) this.httpSession.getAttribute("res_username");
        onlineUsers.put(res_username, this);

        String message = MessageUtils.getMessage(true, null, getNames());
        broadcastAllUsers(message);
        addOnlineCount();
    }

    @OnClose
    public void onClose() {
        onlineUsers.remove(this.sid);
        subOnlineCount();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (StringUtils.isEmpty(message)) {
            for (ChatEndpoint socket : onlineUsers.values()) {
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
        ChatEndpoint.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ChatEndpoint.onlineCount--;
    }
}
