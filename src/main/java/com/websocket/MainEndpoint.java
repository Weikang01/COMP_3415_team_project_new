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
    private static final ConcurrentHashMap<Integer, MainEndpoint> onlineResidents = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Resident> onlineResidentsInfo = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, MainEndpoint> onlineDoctors = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Integer, Doctor> onlineDoctorsInfo = new ConcurrentHashMap<>();
    private Session session;
    private HttpSession httpSession;
    private boolean is_resident;
    private int id;

    private Set<Integer> getResidentNames() {
        return onlineResidents.keySet();
    }
    private Set<Integer> getDoctorNames() {
        return onlineDoctors.keySet();
    }

    private void broadcastAllDoctors(String message) {
        _broadcastAll(message, onlineDoctors);

    }
    private void broadcastAllResidents(String message) {
        _broadcastAll(message, onlineResidents);
    }

    private void _broadcastAll(String message, ConcurrentHashMap<Integer, MainEndpoint> onlineUsers) {
        try {
            Set<Integer> ids = onlineUsers.keySet();
            for (int name : ids) {
                MainEndpoint mainEndpoint = onlineUsers.get(name);
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
            System.out.println("A resident logged in");
            Resident resident = (Resident) obj_res;
            id = resident.getId();
            onlineResidents.put(id, this);
            onlineResidentsInfo.put(id, resident);
            is_resident = true;
            String doctorListMessage = MessageUtils.getMessage(true, null, getDoctorNames());
            String newResidentMessage = MessageUtils.getMessage(true, id, "new");
            try {
                this.session.getBasicRemote().sendText(doctorListMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            broadcastAllDoctors(newResidentMessage);
        } else {
            System.out.println("A doctor logged in");
            Doctor doctor = (Doctor) this.httpSession.getAttribute("doctor");
            id = doctor.getId();
            onlineDoctors.put(id, this);
            onlineDoctorsInfo.put(id, doctor);
            is_resident = false;
            String residentListMessage = MessageUtils.getMessage(true, null, getResidentNames());
            String newDoctorMessage = MessageUtils.getMessage(true, id, "new");
            try {
                this.session.getBasicRemote().sendText(residentListMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            broadcastAllResidents(newDoctorMessage);
        }
    }

    @OnClose
    public void onClose() {
        if (is_resident) {
            onlineResidents.remove(this.id);
            onlineResidentsInfo.remove(this.id);
        } else {
            onlineDoctors.remove(this.id);
            onlineDoctorsInfo.remove(this.id);
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
