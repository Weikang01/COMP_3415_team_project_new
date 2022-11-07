package com.servlet;

import com.bean.Doctor;
import com.bean.DoctorSpecialty;
import com.bean.Resident;
import com.dao.DoctorDAO;
import com.dao.DoctorSpecialtyDAO;
import com.dao.HospitalDAO;
import com.dao.SpecialtyDAO;
import com.dao.impl.DoctorDAOImpl;
import com.dao.impl.DoctorSpecialtyDAOImpl;
import com.dao.impl.HospitalDAOImpl;
import com.dao.impl.SpecialtyDAOImpl;
//import com.logic.OnlineStatusLogic;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springmvc.ViewBaseServlet;
import com.utils.DoctorUtils;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/res.home")
public class ResServlet extends ViewBaseServlet {
    DoctorDAO doctorDAO = new DoctorDAOImpl();
    DoctorSpecialtyDAO doctorSpecialtyDAO = new DoctorSpecialtyDAOImpl();
    HospitalDAO hospitalDAO = new HospitalDAOImpl();
    SpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getSession(false) == null || req.getSession().getAttribute("resident") == null) {
            resp.sendRedirect("welcome");
        } else {
            Resident resident = (Resident) req.getSession().getAttribute("resident");
            List<Doctor> doctorsNearby = doctorDAO.getDoctorsNearCoord(resident.getLatitude(), resident.getLongitude(), 100);
            List<Boolean> doctorsOnlineStatus = new ArrayList<>();

            req.getSession().setMaxInactiveInterval(30 * 60);
            req.getSession().setAttribute("doctorsNearby", DoctorUtils.doctorListJson(doctorsNearby));
            req.getSession().setAttribute("doctorsOnlineStatus", doctorsOnlineStatus);
            processTemplate("res_main", req, resp);
        }
    }
}
