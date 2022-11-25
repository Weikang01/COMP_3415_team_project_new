package com.servlet;

import com.bean.FamilyHistoryForm;
import com.dao.FamilyHistoryFormDAO;
import com.dao.impl.FamilyHistoryFormDAOImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/FamilyHistoryFormDAOServlet")
public class FamilyHistoryFormDAOServlet extends HttpServlet {

    FamilyHistoryFormDAO familyHistoryFormDAO = new FamilyHistoryFormDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        StringBuilder json = new StringBuilder();

        switch (req.getParameter("item"))
        {
            case "getFamilyHistoryFormList":
                System.out.println("resident_id: " + req.getParameter("resident_id"));
                List<FamilyHistoryForm> familyHistoryFormList = familyHistoryFormDAO.getFamilyHistoryFormList(Integer.parseInt(req.getParameter("resident_id")));
                json.append("{\"length\":").append(familyHistoryFormList.size());

                for (int i = 0; i < familyHistoryFormList.size(); i++) {
                    json.append(",\"").append(i).append("\":{");

                    json.append("\"resident_id\":").append(familyHistoryFormList.get(i).getResident_id()).append(",");
                    json.append("\"relationship\":\"").append(familyHistoryFormList.get(i).getRelation()).append("\",");
                    json.append("\"age\":").append(familyHistoryFormList.get(i).getAge()).append(",");
                    json.append("\"is_deceased\":\"").append(familyHistoryFormList.get(i).isIs_deceased()).append("\",");
                    json.append("\"health_condition\":\"").append(familyHistoryFormList.get(i).getHealth_condition()).append("\",");
                    json.append("\"death_age\":").append(familyHistoryFormList.get(i).getDeath_age()).append(",");
                    json.append("\"death_cause\":\"").append(familyHistoryFormList.get(i).getDeath_cause()).append("\"");

                    json.append("}");
                }

                json.append("}");
                break;
        }
        out.write(json.toString());
        out.close();
    }
}
