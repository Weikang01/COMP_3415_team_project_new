package com.servlet;

import com.bean.Doctor;
import com.bean.Hospital;
import com.bean.Resident;
import com.bean.Specialty;
import com.dao.DoctorDAO;
import com.dao.DoctorSpecialtyDAO;
import com.dao.HospitalDAO;
import com.dao.SpecialtyDAO;
import com.dao.impl.DoctorDAOImpl;
import com.dao.impl.DoctorSpecialtyDAOImpl;
import com.dao.impl.HospitalDAOImpl;
import com.dao.impl.SpecialtyDAOImpl;
//import com.logic.OnlineStatusLogic;
import com.springmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/doc_reg")
public class DocRegServlet extends ViewBaseServlet {
    DoctorDAO doctorDAO = new DoctorDAOImpl();
    DoctorSpecialtyDAO doctorSpecialtyDAO = new DoctorSpecialtyDAOImpl();
    HospitalDAO hospitalDAO = new HospitalDAOImpl();
    SpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Hospital> hospitals = hospitalDAO.getHospitalList();
        req.getSession().setAttribute("hospitals", hospitals);
        List<Specialty> specialties = specialtyDAO.getSpecialtyList();
        req.getSession().setAttribute("specialties", specialties);
        super.processTemplate("doc_reg", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Doctor doctor = new Doctor();

        doctor.setUsername(req.getParameter("username"));
        doctor.setPassword(req.getParameter("password"));
        doctor.setFirstname(req.getParameter("firstname"));
        doctor.setLastname(req.getParameter("lastname"));
        doctor.setAddress(req.getParameter("address"));
        doctor.setLatitude(Double.parseDouble(req.getParameter("latitude")));
        doctor.setLongitude(Double.parseDouble(req.getParameter("longitude")));
        doctor.setTel(req.getParameter("tel"));
        doctor.setHospital_id(Integer.parseInt(req.getParameter("hospital_id")));

        doctorDAO.insert(doctor);
        doctor = doctorDAO.getDoctorByUsername(doctor.getUsername());

        int[] specialty_ids = new int[]{
                Integer.parseInt(req.getParameter("specialty1_id")),
                Integer.parseInt(req.getParameter("specialty2_id")),
                Integer.parseInt(req.getParameter("specialty3_id")),
                Integer.parseInt(req.getParameter("specialty4_id")),
                Integer.parseInt(req.getParameter("specialty5_id"))
        };

        for (int specialty_id : specialty_ids) {
            if (specialty_id != 0) {
                doctorSpecialtyDAO.addNewSpecialty(doctor.getId(), specialty_id, 0);
            }
        }

        req.getSession().setAttribute("doctor", doctor);
        resp.sendRedirect("doc.home");
    }
}
