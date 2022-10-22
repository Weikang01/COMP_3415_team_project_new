package com.servlet;

import com.bean.Resident;
import com.dao.ResidentDAO;
import com.dao.impl.ResidentDAOImpl;
import com.logic.OnlineStatusLogic;
import com.springmvc.ViewBaseServlet;
import com.utils.DateUtils;
import com.utils.Enums;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Description: TODO
 * @author: Weikang
 * @version: 1.0
 * @date: 29/09/2022 12:31
 */
@WebServlet("/res_reg")
public class ResRegServlet extends ViewBaseServlet {
    ResidentDAO residentDAO = new ResidentDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("genders", Enums.getGenders());
        super.processTemplate("res_reg", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Resident resident = new Resident();

        resident.setUsername(req.getParameter("username"));
        resident.setPassword(req.getParameter("password"));
        resident.setFirstname(req.getParameter("firstname"));
        resident.setLastname(req.getParameter("lastname"));
        resident.setBirthdate(DateUtils.parseDate(req.getParameter("birthdate")));

        resident.setAddress(req.getParameter("address"));
        resident.setLatitude(Double.parseDouble(req.getParameter("latitude")));
        resident.setLongitude(Double.parseDouble(req.getParameter("longitude")));
        resident.setTel(req.getParameter("tel"));

        residentDAO.insert(resident);

        HttpSession session = req.getSession(true);
        session.setAttribute("resident", resident);

        OnlineStatusLogic.userLogin(Resident.class, resident.getUsername());
        resp.sendRedirect("res.home");
    }
}
