package com.websocket;

import com.bean.Doctor;
import com.bean.Resident;
import com.utils.MessageUtils;
import com.utils.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/main", configurator = GetHttpSessionConfigurator.class)
public class MainEndpoint {
    private static final ConcurrentHashMap<String, MainEndpoint> onlineResidents = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Resident> onlineResidentsInfo = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, MainEndpoint> onlineDoctors = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Doctor> onlineDoctorsInfo = new ConcurrentHashMap<>();
    private Session session;
    private HttpSession httpSession;
    private boolean is_resident;
    private String username;

    private Set<String> getNames() {
        return onlineResidents.keySet();
    }

    private void broadcastAllUsers(String message) {
        if (is_resident) {
            _broadcastAllUsers(message, onlineResidents);
        } else {
            _broadcastAllUsers(message, onlineDoctors);
        }

    }

    private void _broadcastAllUsers(String message, ConcurrentHashMap<String, MainEndpoint> onlineDoctors) {
        try {
            Set<String> names = onlineDoctors.keySet();
            for (String name : names) {
                MainEndpoint mainEndpoint = onlineDoctors.get(name);
                mainEndpoint.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        Object obj_res = this.httpSession.getAttribute("resident");
        if (obj_res != null) {
            Resident resident = (Resident) obj_res;
            username = resident.getUsername();
            onlineResidents.put(username, this);
            onlineResidentsInfo.put(username, resident);
            is_resident = true;
        } else {
            Doctor doctor = (Doctor) this.httpSession.getAttribute("doctor");
            username = doctor.getUsername();
            onlineDoctors.put(username, this);
            onlineDoctorsInfo.put(username, doctor);
            is_resident = false;
        }
        String message = MessageUtils.getMessage(true, null, getNames());
        broadcastAllUsers(message);
    }

    @OnClose
    public void onClose() {
        if (is_resident) {
            onlineResidents.remove(this.username);
            onlineResidentsInfo.remove(this.username);
        } else {
            onlineDoctors.remove(this.username);
            onlineDoctorsInfo.remove(this.username);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (StringUtils.isEmpty(message)) {
            for (MainEndpoint socket : onlineResidents.values()) {
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
}
