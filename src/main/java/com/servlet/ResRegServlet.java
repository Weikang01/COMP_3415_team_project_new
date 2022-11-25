package com.servlet;

import com.bean.FamilyHistoryForm;
import com.bean.PatientHistoryForm;
import com.bean.Resident;
import com.dao.FamilyHistoryFormDAO;
import com.dao.PatientHistoryFormDAO;
import com.dao.ResidentDAO;
import com.dao.impl.FamilyHistoryFormDAOImpl;
import com.dao.impl.PatientHistoryFormDAOImpl;
import com.dao.impl.ResidentDAOImpl;
//import com.logic.OnlineStatusLogic;
import com.springmvc.ViewBaseServlet;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.utils.DateUtils;
import com.utils.Enums;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @author: Weikang
 * @version: 1.0
 * @date: 29/09/2022 12:31
 */
@WebServlet("/res_reg")
public class ResRegServlet extends ViewBaseServlet {
    ResidentDAO residentDAO = new ResidentDAOImpl();
    PatientHistoryFormDAO patientHistoryFormDAO = new PatientHistoryFormDAOImpl();
    FamilyHistoryFormDAO familyHistoryFormDAO = new FamilyHistoryFormDAOImpl();

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
        resident.setId(residentDAO.getResidentByUsername(resident.getUsername()).getId());

        PatientHistoryForm patientHistoryForm = new PatientHistoryForm();
        patientHistoryForm.setResident_id(resident.getId());
        patientHistoryForm.setHad_ECT(Boolean.parseBoolean(req.getParameter("had_drug_allergies")));
        patientHistoryForm.setHad_psychotherapy(Boolean.parseBoolean(req.getParameter("had_psychotherapy")));
        patientHistoryForm.setHad_drug_allergy(Boolean.parseBoolean(req.getParameter("had_drug_allergies")));
        patientHistoryForm.setDrug_allergy(req.getParameter("drug_allergies_text"));

        patientHistoryFormDAO.createNewPatientHistoryForm(patientHistoryForm);

        String family_relationship_text = "family_relationship";
        int idx = 0;
        String family_relationship = req.getParameter(family_relationship_text + idx);
        while (family_relationship != null) {
            FamilyHistoryForm familyHistoryForm = new FamilyHistoryForm();

            familyHistoryForm.setResident_id(resident.getId());
            familyHistoryForm.setRelation(family_relationship);
            familyHistoryForm.setAge(Integer.parseInt(req.getParameter("family_member_age" + idx)));
            familyHistoryForm.setHealth_condition(req.getParameter("family_member_health_condition" + idx));
            familyHistoryForm.setIs_deceased(Boolean.parseBoolean(req.getParameter("is_deceased" + idx)));
            familyHistoryForm.setDeath_age(Integer.parseInt(req.getParameter("death_age" + idx)));
            familyHistoryForm.setDeath_cause(req.getParameter("death_cause" + idx));

            familyHistoryFormDAO.createNewFamilyHistoryForm(familyHistoryForm);
            idx ++;
            family_relationship = req.getParameter(family_relationship_text + idx);
        }

        resp.sendRedirect("res.home");
    }
}
