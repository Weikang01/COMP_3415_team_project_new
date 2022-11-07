package com.servlet;

import com.bean.Doctor;
import com.bean.Resident;
//import com.logic.OnlineStatusLogic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/online_checker")
public class OnlineCheckerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object db = req.getParameter("db");
        req.getSession().setMaxInactiveInterval(30 * 60);
    }
}
