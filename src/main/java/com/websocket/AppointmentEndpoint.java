package com.websocket;

import com.bean.Appointment;
import com.bean.AppointmentStatus;
import com.bean.Doctor;
import com.bean.Resident;
import com.dao.AppointmentDAO;
import com.dao.impl.AppointmentDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.DateUtils;
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
    private final AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private Session session;
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
        this.session = session;

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
        ObjectMapper mapper = new ObjectMapper();
        try {
            Map<String, Object> map = mapper.readValue(message, Map.class);
            String type = (String) map.get("type");
            map.put("message", "make_appointment");
            map.put("system", String.valueOf(true));
            int toId = 0;

            if (type.equals("new")) {
                Appointment appointment = new Appointment();
                appointment.setReason((String) map.get("reason"));
                appointment.setDate(DateUtils.parseDate((String) map.get("date")));
                appointment.setHour((Integer) map.get("hour"));
                appointment.setMin((Integer) map.get("min"));
                appointment.setResident_id((Integer) map.get("resident_id"));
                appointment.setDoctor_id((Integer) map.get("doctor_id"));

                if (isResident) {
                    appointment.setStatus(AppointmentStatus.pending.getValue());
                    toId = appointment.getDoctor_id();
                } else {
                    appointment.setStatus(AppointmentStatus.confirmed.getValue());
                    toId = appointment.getResident_id();
                }

                appointmentDAO.insert(appointment);
            }
            else {
                toId = (Integer) map.get("toId");
                if (isResident) {
                    toChatEndpoint = ChatEndpoint.onlineDoctors.get((Integer) map.get("toId"));
                    toMainEndpoint = MainEndpoint.onlineDoctors.get((Integer) map.get("toId"));
                } else {
                    toChatEndpoint = ChatEndpoint.onlineResidents.get((Integer) map.get("toId"));
                    toMainEndpoint = MainEndpoint.onlineResidents.get((Integer) map.get("toId"));
                }
            }
            System.out.println("toId: " + toId);

            if (isResident) {
                if (onlineDoctors.get(toId) != null)
                    onlineDoctors.get(toId).sendMessage(mapper.writeValueAsString(map));
            } else {
                if (onlineResidents.get(toId) != null)
                    onlineResidents.get(toId).sendMessage(mapper.writeValueAsString(map));
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

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
}
