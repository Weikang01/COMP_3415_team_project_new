package com.websocket;

import com.bean.Doctor;
import com.bean.Resident;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.StringUtils;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value ="/make_appointment/{param}", configurator = GetHttpSessionConfigurator.class)
public class AppointmentEndpoint {
    static final ConcurrentHashMap<Integer, AppointmentEndpoint> onlineResidents = new ConcurrentHashMap<>();
    static final ConcurrentHashMap<Integer, AppointmentEndpoint> onlineDoctors = new ConcurrentHashMap<>();
    private HttpSession httpSession;
    private int fromId;
    private int toId;
    private boolean isResident;
    private ChatEndpoint toChatEndpoint;
    private MainEndpoint toMainEndpoint;
    private int id;
    @OnOpen
    public void onOpen(Session session, @PathParam("param")String param, EndpointConfig config)
    {
        Map<String, String> queryMap = StringUtils.getQueryMap(session.getQueryString());

        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        Object obj_res = this.httpSession.getAttribute("resident");

        if (obj_res != null) {
            isResident = true;
            fromId = Integer.parseInt(queryMap.get("resident_id"));
            toId = Integer.parseInt(queryMap.get("doctor_id"));
            toChatEndpoint = ChatEndpoint.onlineDoctors.get(toId);
            toMainEndpoint = MainEndpoint.onlineDoctors.get(toId);
            Resident resident = (Resident) obj_res;
            id = resident.getId();
            onlineResidents.put(id, this);
        } else {
            isResident = false;
            toId = Integer.parseInt(queryMap.get("resident_id"));
            fromId = Integer.parseInt(queryMap.get("doctor_id"));
            toChatEndpoint = ChatEndpoint.onlineResidents.get(toId);
            toMainEndpoint = MainEndpoint.onlineResidents.get(toId);
            Doctor doctor = (Doctor) this.httpSession.getAttribute("doctor");
            id = doctor.getId();
            onlineDoctors.put(id, this);
        }
    }

    @OnClose
    public void onClose()
    {
        if (isResident) {
            onlineResidents.remove(this.id);
        } else {
            onlineDoctors.remove(this.id);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session)
    {
        System.out.println(message);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, String> map = mapper.readValue(message, Map.class);
            String type = map.get("type");
            map.put("message", "make_appointment");
            map.put("system", String.valueOf(true));

            if (!type.equals("new")) {
                System.out.println("toId: " + map.get("toId"));
                if (isResident) {
                    toChatEndpoint = ChatEndpoint.onlineDoctors.get(Integer.parseInt(map.get("toId")));
                    toMainEndpoint = MainEndpoint.onlineDoctors.get(Integer.parseInt(map.get("toId")));
                } else {
                    toChatEndpoint = ChatEndpoint.onlineResidents.get(Integer.parseInt(map.get("toId")));
                    toMainEndpoint = MainEndpoint.onlineResidents.get(Integer.parseInt(map.get("toId")));
                }
            }

            if (toChatEndpoint != null) {
                toChatEndpoint.sendMessage(mapper.writeValueAsString(map));
            }
            if (toMainEndpoint != null) {
                toMainEndpoint.sendMessage(mapper.writeValueAsString(map));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error)
    {
        error.printStackTrace();
    }
}
