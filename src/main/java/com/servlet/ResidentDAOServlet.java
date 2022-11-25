package com.servlet;

import com.bean.Resident;
import com.dao.ResidentDAO;
import com.dao.impl.ResidentDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/ResidentDAOServlet")
public class ResidentDAOServlet extends HttpServlet {
    ResidentDAO residentDAO = new ResidentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        String resident_id_str = req.getParameter("resident_id");
        if (resident_id_str != null) {
            Resident resident = residentDAO.getResidentById(Integer.parseInt(resident_id_str));
            out.write(mapper.writeValueAsString(resident));
        }

        out.close();
    }
}
