package com.servlet;

import com.bean.PatientHistoryForm;
import com.dao.PatientHistoryFormDAO;
import com.dao.impl.PatientHistoryFormDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/PatientHistoryFormDAOServlet")
public class PatientHistoryFormDAOServlet extends HttpServlet {
    PatientHistoryFormDAO patientHistoryFormDAO = new PatientHistoryFormDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        ObjectMapper mapper = new ObjectMapper();

        String resident_id_str = req.getParameter("resident_id");
        if (resident_id_str != null) {
            PatientHistoryForm mostRecentHistoryForm = patientHistoryFormDAO.getMostRecentHistoryForm(Integer.parseInt(resident_id_str));
            out.write(mapper.writeValueAsString(mostRecentHistoryForm));
        }

        out.close();
    }
}
