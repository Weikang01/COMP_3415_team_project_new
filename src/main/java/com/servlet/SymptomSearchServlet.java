package com.servlet;

import com.bean.Symptom;
import com.dao.SymptomDAO;
import com.dao.impl.SymptomDAOImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/symptomSearch")
public class SymptomSearchServlet extends HttpServlet {
    SymptomDAO symptomDAO = new SymptomDAOImpl();
    List<Symptom> symptoms = null;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (symptoms == null) {
            symptoms = symptomDAO.getSymptomList();
        }

        Map<String, Symptom> value_map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < symptoms.size(); i++) {
            value_map.put("" + i, symptoms.get(i));
        }

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.write(objectMapper.writeValueAsString(value_map));
        out.close();
    }
}
