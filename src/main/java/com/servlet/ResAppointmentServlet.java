package com.servlet;

import com.bean.Appointment;
import com.dao.AppointmentDAO;
import com.dao.impl.AppointmentDAOImpl;
import com.utils.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/make_appointment")
public class ResAppointmentServlet extends HttpServlet {
    AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Appointment appointment = new Appointment();

        appointment.setDate(DateUtils.parseDate(req.getParameter("date")));
        appointment.setHour(Integer.parseInt(req.getParameter("hour")));
        appointment.setMin(Integer.parseInt(req.getParameter("min")));

        appointmentDAO.insert(appointment);

        req.setAttribute("success", "true");
    }


}
