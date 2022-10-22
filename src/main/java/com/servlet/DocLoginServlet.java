package com.servlet;

import com.bean.Doctor;
import com.dao.DoctorDAO;
import com.dao.impl.DoctorDAOImpl;
//import com.logic.OnlineStatusLogic;
import com.springmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/doc_login")
public class DocLoginServlet extends ViewBaseServlet {
    DoctorDAO doctorDAO = new DoctorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.processTemplate("doc_login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (doctorDAO.checkLoginInfo(username, password)) {
            Doctor doctor = doctorDAO.getDoctorByUsername(username);

            HttpSession session = req.getSession(true);
            session.setAttribute("doctor", doctor);

//            OnlineStatusLogic.userLogin(Doctor.class, doctor.getUsername());
            resp.sendRedirect("doc.home");
        } else {
            HttpSession session = req.getSession(true);
            session.setAttribute("invalid_input", "1");
            resp.sendRedirect("doc_login");
        }
    }
}
