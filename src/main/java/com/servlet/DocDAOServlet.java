package com.servlet;

import com.bean.Doctor;
import com.bean.Resident;
import com.dao.DoctorDAO;
import com.dao.HospitalDAO;
import com.dao.SpecialtyDAO;
import com.dao.impl.DoctorDAOImpl;
import com.dao.impl.HospitalDAOImpl;
import com.dao.impl.SpecialtyDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/docdao")
public class DocDAOServlet extends HttpServlet {
    DoctorDAO doctorDAO = new DoctorDAOImpl();
    HospitalDAO hospitalDAO = new HospitalDAOImpl();
    SpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    static private final int defaultRange = 1000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HttpSession session = req.getSession();
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder json = new StringBuilder();
        switch (req.getParameter("item")) {
            case "getDoctorsNearCoord":
                Resident resident = (Resident) req.getSession().getAttribute("resident");
                List<Doctor> doctorsNearCoord = doctorDAO.getDoctorsNearCoord(resident.getLatitude(), resident.getLongitude(), defaultRange);
                json = new StringBuilder("{ \"length\": " + doctorsNearCoord.size() + ", ");

                for (int i = 0; i < doctorsNearCoord.size(); i++) {
                    String doctor = mapper.writeValueAsString(doctorsNearCoord.get(i));
                    json.append("\"_").append(i).append("\" : ").append(doctor);
                    if (i != doctorsNearCoord.size() - 1) {
                        json.append(",");
                    }
                }
                json.append("}");
                break;
            case "getDoctorById":
                String doctor_id;
                try {
                    doctor_id = req.getParameter("doctor_id");
                } catch (Exception e) {
                    return;
                }
                json = new StringBuilder(mapper.writeValueAsString(doctorDAO.getDoctorById(Integer.parseInt(doctor_id))));
                break;
            case "getDoctorByUsername":
                String doctor_username;
                try {
                    doctor_username = req.getParameter("doctor_username");
                } catch (Exception e) {
                    return;
                }
                json = new StringBuilder(mapper.writeValueAsString(doctorDAO.getDoctorByUsername(doctor_username)));
                break;
        }
        out.write(json.toString());
        out.close();
    }
}
