package com.websocket;

import com.bean.Doctor;
import com.bean.Message;
import com.bean.Resident;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.MessageUtils;
import com.utils.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value ="/chat/{param}", configurator = GetHttpSessionConfigurator.class)
public class ChatEndpoint {
    static final ConcurrentHashMap<Integer, ChatEndpoint> onlineResidents = new ConcurrentHashMap<>();
    static final ConcurrentHashMap<Integer, Resident> onlineResidentsInfo = new ConcurrentHashMap<>();
    static final ConcurrentHashMap<Integer, ChatEndpoint> onlineDoctors = new ConcurrentHashMap<>();
    static final ConcurrentHashMap<Integer, Doctor> onlineDoctorsInfo = new ConcurrentHashMap<>();
    private Session session;
    private HttpSession httpSession;
    private boolean is_resident;
    private int id;
    private int o_id;

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
    private void sendMessageBack(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void _broadcastAll(String message, ConcurrentHashMap<Integer, ChatEndpoint> onlineUsers) {
        try {
            Set<Integer> ids = onlineUsers.keySet();
            for (int name : ids) {
                ChatEndpoint chatEndpoint = onlineUsers.get(name);
                chatEndpoint.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("param")String param, EndpointConfig config) {

//        id = Integer.parseInt(param);
        Map<String, String> queryMap = StringUtils.getQueryMap(session.getQueryString());

        this.session = session;
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        Object obj_res = this.httpSession.getAttribute("resident");

        if (obj_res != null) {
            id = Integer.parseInt(queryMap.get("resident_id"));
            System.out.println("resident " + id + " entered chat");
            o_id = Integer.parseInt(queryMap.get("doctor_id"));

            System.out.println("A resident logged in");
            Resident resident = (Resident) obj_res;
            id = resident.getId();
            onlineResidents.put(id, this);
            onlineResidentsInfo.put(id, resident);
            is_resident = true;
        } else {
            id = Integer.parseInt(queryMap.get("doctor_id"));
            o_id = Integer.parseInt(queryMap.get("resident_id"));
            System.out.println("doctor " + id + " entered chat");
            System.out.println("A doctor logged in");
            Doctor doctor = (Doctor) this.httpSession.getAttribute("doctor");
            id = doctor.getId();
            onlineDoctors.put(id, this);
            onlineDoctorsInfo.put(id, doctor);
            is_resident = false;
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
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message msg = mapper.readValue(message, Message.class);
            System.out.println(msg);
            String msg_text = msg.getMessage();
            String result_msg;
            if (is_resident) {
                result_msg = MessageUtils.getMessage(false, id,
                        onlineResidentsInfo.get(id).getFirstname(),onlineResidentsInfo.get(id).getLastname(), msg_text);
            } else {
                result_msg = MessageUtils.getMessage(false, id,
                        onlineDoctorsInfo.get(id).getFirstname(),onlineDoctorsInfo.get(id).getLastname(), msg_text);
            }

            System.out.println(result_msg);
            System.out.println(msg);

            if (msg.isToResident()) {
                ChatEndpoint chatEndpoint = onlineResidents.get(msg.getToId());
                if (chatEndpoint != null)
                    chatEndpoint.session.getBasicRemote().sendText(result_msg);
            } else {
                ChatEndpoint chatEndpoint = onlineDoctors.get(msg.getToId());
                if (chatEndpoint == null) {
                    MainEndpoint mainEndpoint = MainEndpoint.onlineDoctors.get(msg.getToId());
                    if (mainEndpoint != null) {
                        mainEndpoint.sendMessage(result_msg);
                    }
                } else {
                    chatEndpoint.session.getBasicRemote().sendText(result_msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
