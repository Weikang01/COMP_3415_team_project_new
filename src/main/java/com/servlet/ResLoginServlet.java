package com.servlet;

import com.bean.Resident;
import com.dao.ResidentDAO;
import com.dao.impl.ResidentDAOImpl;
//import com.logic.OnlineStatusLogic;
import com.springmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/res_login")
public class ResLoginServlet extends ViewBaseServlet {
    ResidentDAO residentDAO = new ResidentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.processTemplate("res_login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (residentDAO.checkLoginInfo(username, password)) {
            Resident resident = residentDAO.getResidentByUsername(username);

            HttpSession session = req.getSession(true);
            session.setAttribute("resident", resident);

//            OnlineStatusLogic.userLogin(Resident.class, username);
            resp.sendRedirect("res.home");
        } else {
            HttpSession session = req.getSession(true);
            session.setAttribute("invalid_input", "1");
            resp.sendRedirect("res_login");
        }
    }
}
