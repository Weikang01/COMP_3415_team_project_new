package com.servlet;

import com.dao.DoctorDAO;
import com.dao.impl.DoctorDAOImpl;
import com.springmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chat/from_res")
public class ResChatServlet extends ViewBaseServlet {
    DoctorDAO doctorDAO = new DoctorDAOImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("cur_doctor", doctorDAO.getDoctorById(Integer.parseInt(req.getParameter("doctor_id"))));
        super.processTemplate("chat/res_chat", req, resp);
    }
}
