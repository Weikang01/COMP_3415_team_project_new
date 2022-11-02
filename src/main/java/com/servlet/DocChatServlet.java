package com.servlet;

import com.bean.Resident;
import com.dao.DoctorDAO;
import com.dao.ResidentDAO;
import com.dao.impl.DoctorDAOImpl;
import com.dao.impl.ResidentDAOImpl;
import com.springmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chat/from_doc")
public class DocChatServlet extends ViewBaseServlet {
    ResidentDAO residentDAO = new ResidentDAOImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Resident resident = residentDAO.getResidentById(Integer.parseInt(req.getParameter("resident_id")));
        req.getSession().setAttribute("cur_resident", resident);
        super.processTemplate("chat/doc_chat", req, resp);
    }
}
