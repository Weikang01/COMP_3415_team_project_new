package com.servlet;

import com.bean.Resident;
import com.dao.ResidentDAO;
import com.dao.impl.ResidentDAOImpl;
import com.springmvc.ViewBaseServlet;
import com.utils.DateUtils;
import com.utils.Enums;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/res_edit")
public class ResEditServlet extends ViewBaseServlet {
    ResidentDAO residentDAO = new ResidentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object resident = req.getSession().getAttribute("resident");
        if (resident == null) {
            resp.sendRedirect("/welcome");
        } else {
            req.getSession().setAttribute("genders", Enums.getGenders());
            super.processTemplate("res_edit", req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Resident resident = (Resident) req.getSession().getAttribute("resident");

        resident.setFirstname(req.getParameter("firstname"));
        resident.setLastname(req.getParameter("lastname"));
        resident.setGender(Integer.parseInt(req.getParameter("gender")));
        resident.setAddress(req.getParameter("address"));
        resident.setBirthdate(DateUtils.parseDate(req.getParameter("birthdate")));
        resident.setLatitude(Double.parseDouble(req.getParameter("latitude")));
        resident.setLongitude(Double.parseDouble(req.getParameter("longitude")));
        resident.setTel(req.getParameter("tel"));
        System.out.println("resident updated: " + resident);
        residentDAO.update(resident);

        req.getSession().setAttribute("resident", resident);
        resp.sendRedirect("res.home");
    }
}
