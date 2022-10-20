package com.servlet;

import com.dao.DoctorDAO;
import com.dao.ResidentDAO;
import com.dao.impl.DoctorDAOImpl;
import com.dao.impl.ResidentDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/username_checking")
public class UsernameCheckingServlet extends HttpServlet {
    ResidentDAO residentDAO = new ResidentDAOImpl();
    DoctorDAO doctorDAO = new DoctorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String db = req.getParameter("db");
        String username = req.getParameter("username");
        boolean usernameExist = false;
        if (db.equals("res")) {
            usernameExist = residentDAO.isUsernameExist(username);
        } else if (db.equals("doc")) {
            usernameExist = doctorDAO.isUsernameExist(username);
        } else {
            throw new IllegalAccessError();
        }

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.write(usernameExist? "1": "0");
        out.close();
    }
}
