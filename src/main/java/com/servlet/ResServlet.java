package com.servlet;

import com.bean.Doctor;
import com.bean.Resident;
import com.dao.DoctorDAO;
import com.dao.HospitalDAO;
import com.dao.SpecialtyDAO;
import com.dao.impl.DoctorDAOImpl;
import com.dao.impl.HospitalDAOImpl;
import com.dao.impl.SpecialtyDAOImpl;
import com.logic.OnlineStatusLogic;
import com.springmvc.ViewBaseServlet;

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
            for (Doctor doctor : doctorsNearby) {
                doctor.setHospital(hospitalDAO.getHospitalById(doctor.getHospital_id()).getName());
                doctor.setSpecialty1(specialtyDAO.getSpecialtyById(doctor.getSpecialty1_id()).getName());
                doctor.setSpecialty2(specialtyDAO.getSpecialtyById(doctor.getSpecialty2_id()).getName());
                doctor.setSpecialty3(specialtyDAO.getSpecialtyById(doctor.getSpecialty3_id()).getName());
                doctor.setSpecialty4(specialtyDAO.getSpecialtyById(doctor.getSpecialty4_id()).getName());
                doctor.setSpecialty5(specialtyDAO.getSpecialtyById(doctor.getSpecialty5_id()).getName());
                doctorsOnlineStatus.add(OnlineStatusLogic.isOnline(Doctor.class, doctor.getUsername()));
            }
            req.getSession().setMaxInactiveInterval(30 * 60);
            req.getSession().setAttribute("doctorsNearby", doctorsNearby);
            req.getSession().setAttribute("doctorsOnlineStatus", doctorsOnlineStatus);
            processTemplate("res_main", req, resp);
        }
    }
}
