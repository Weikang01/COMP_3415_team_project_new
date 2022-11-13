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
import com.springmvc.ViewBaseServlet;
import com.utils.DateUtils;
import com.utils.Enums;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/doc_edit")
public class DocEditServlet extends ViewBaseServlet {
    DoctorDAO doctorDAO = new DoctorDAOImpl();
    HospitalDAO hospitalDAO = new HospitalDAOImpl();
    SpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    DoctorSpecialtyDAO doctorSpecialtyDAO = new DoctorSpecialtyDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object doctor = req.getSession().getAttribute("doctor");
        if (doctor == null) {
            resp.sendRedirect("/welcome");
        } else {
            req.getSession().setAttribute("hospitals", hospitalDAO.getHospitalList());
            req.getSession().setAttribute("specialties", specialtyDAO.getSpecialtyList());
            req.getSession().setAttribute("doc_specialties", doctorSpecialtyDAO.getAllByDoctorId(((Doctor) doctor).getId()));

            super.processTemplate("doc_edit", req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Doctor doctor = (Doctor) req.getSession().getAttribute("doctor");

        doctor.setFirstname(req.getParameter("firstname"));
        doctor.setLastname(req.getParameter("lastname"));
        doctor.setAddress(req.getParameter("address"));
        doctor.setLatitude(Double.parseDouble(req.getParameter("latitude")));
        doctor.setLongitude(Double.parseDouble(req.getParameter("longitude")));
        doctor.setTel(req.getParameter("tel"));
        doctor.setHospital_id(Integer.parseInt(req.getParameter("hospital_id")));

        Set<Integer> specialty_ids = new HashSet<Integer>();

        for (int i = 1; i <= 5; i++) {
            int val = Integer.parseInt(req.getParameter("specialty" + i + "_id"));
            if (val != 0) {
                specialty_ids.add(val);
            }
        }

        List<DoctorSpecialty> prev_SpecialtyObjs = doctorSpecialtyDAO.getAllByDoctorId(doctor.getId());
        ArrayList<Integer> new_specialties = new ArrayList<>();
        ArrayList<Integer> old_specialties = new ArrayList<>();
        boolean in_new;
        for (DoctorSpecialty prev_specialtyObj : prev_SpecialtyObjs) {
            in_new = false;
            for (int specialty_id : specialty_ids) {
                if (specialty_id == prev_specialtyObj.getSpecialty_id()) {
                    in_new = true;
                    break;
                }
            }
            if (!in_new) {
                old_specialties.add(prev_specialtyObj.getSpecialty_id());
            }
        }

        boolean in_old;
        for (int specialty_id : specialty_ids) {
            in_old = false;
            for (DoctorSpecialty prev_specialtyObj : prev_SpecialtyObjs) {
                if (specialty_id == prev_specialtyObj.getSpecialty_id()) {
                    in_old = true;
                    break;
                }
            }
            if (!in_old) {
                new_specialties.add(specialty_id);
            }
        }

        for (int specialty_id : new_specialties) {
            doctorSpecialtyDAO.addNewSpecialty(doctor.getId(), specialty_id, 0);
        }
        for (int specialty_id : old_specialties) {
            doctorSpecialtyDAO.deleteDoctorSpecialty(doctor.getId(), specialty_id);
        }

        req.getSession().setAttribute("doc_specialties", doctorSpecialtyDAO.getAllByDoctorId(doctor.getId()));
        req.getSession().setAttribute("doctor", doctor);
        resp.sendRedirect("doc.home");
    }
}
