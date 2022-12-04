package com.servlet;

import com.bean.Appointment;
import com.bean.AppointmentStatus;
import com.bean.Doctor;
import com.bean.Resident;
import com.dao.AppointmentDAO;
import com.dao.impl.AppointmentDAOImpl;
import com.utils.AppointmentUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/appointmentDAO")
public class AppointmentDAOServlet extends HttpServlet {
    AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        StringBuilder json = new StringBuilder();
        String id, to_status, from_status;
        switch (req.getParameter("item"))
        {
            case "getAppointmentListByResidentAndDoctor":
                Object residentObj = req.getSession().getAttribute("resident");
                Object doctorObj = req.getSession().getAttribute("doctor");
                if (residentObj == null && doctorObj == null)
                {
                    resp.setStatus(302);
                    out.write("/welcome");
                    out.close();
                    return;
                }
                if (residentObj != null) {
                    Resident resident = (Resident)residentObj;
                    List<Appointment> appointmentList = appointmentDAO.getAppointmentListByResidentId(resident.getId());
                    json.append(AppointmentUtils.appointmentListJson(appointmentList));
                } else {
                    // TODO: doctor
                    Doctor doctor = (Doctor) doctorObj;
                    List<Appointment> appointmentList = appointmentDAO.getAppointmentListByDoctorId(doctor.getId());
                    json.append(AppointmentUtils.appointmentListJson(appointmentList));
                }
                break;
            case "getAppointmentListByResidentAndDoctor2":
                int resident_id = Integer.parseInt(req.getParameter("resident_id"));
                int doctor_id = Integer.parseInt(req.getParameter("doctor_id"));
                System.out.println("resident_id: " + resident_id + "\tdoctor_id: " + doctor_id);
                json.append(AppointmentUtils.appointmentListJson(appointmentDAO.getAppointmentListByResidentAndDoctor(resident_id, doctor_id)));
                break;
            case "getAppointmentStatusList":
                int i = 0;
                int length = AppointmentStatus.values().length;
                json.append("{\"length\": ").append(length).append(",");

                for (AppointmentStatus appointmentStatus : AppointmentStatus.values()) {
                    json.append("\"").append(i)
                            .append("\":{\"name\":\"").append(appointmentStatus.name())
                            .append("\",\"value\":").append(appointmentStatus.getValue())
                            .append("}");
                    i++;
                    if (i != length) {
                        json.append(",");
                    }
                }
                json.append("}");
                break;
            case "changeAppointmentStatus":
                id = req.getParameter("id");
                to_status = req.getParameter("to_status");
                from_status = req.getParameter("from_status");
                if (id != null && to_status != null && AppointmentStatus.isIndexExists(Integer.parseInt(to_status))) {
                    appointmentDAO.updateStatus(Integer.parseInt(to_status), Integer.parseInt(id));
                    json.append("{\"newStatusIndex\":").append(Integer.parseInt(to_status)).append("}");
                } else {
                    json.append("{\"newStatusIndex\":").append(from_status).append("}");
                }
                break;
        }
        out.write(json.toString());
        out.close();
    }
}
