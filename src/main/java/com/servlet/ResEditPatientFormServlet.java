package com.servlet;

import com.bean.FamilyHistoryForm;
import com.bean.PatientHistoryForm;
import com.bean.Resident;
import com.dao.FamilyHistoryFormDAO;
import com.dao.PatientHistoryFormDAO;
import com.dao.impl.FamilyHistoryFormDAOImpl;
import com.dao.impl.PatientHistoryFormDAOImpl;
import com.springmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/res_edit_patient_form")
public class ResEditPatientFormServlet extends ViewBaseServlet {
    PatientHistoryFormDAO patientHistoryFormDAO = new PatientHistoryFormDAOImpl();
    FamilyHistoryFormDAO familyHistoryFormDAO = new FamilyHistoryFormDAOImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Resident resident = (Resident) req.getSession().getAttribute("resident");
        if (resident == null) {
            resp.sendRedirect("/welcome");
        } else {
            PatientHistoryForm mostRecentHistoryForm = patientHistoryFormDAO.getMostRecentHistoryForm(resident.getId());
            req.getSession().setAttribute("mostRecentHistoryForm", mostRecentHistoryForm);
            processTemplate("res_edit_patient_form", req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Resident resident = (Resident) req.getSession().getAttribute("resident");
        if (resident == null) {
            resp.sendRedirect("/welcome");
        } else {
            PatientHistoryForm patientHistoryForm = new PatientHistoryForm();
            patientHistoryForm.setResident_id(resident.getId());
            patientHistoryForm.setHad_ECT(Boolean.parseBoolean(req.getParameter("had_ECT")));
            patientHistoryForm.setHad_psychotherapy(Boolean.parseBoolean(req.getParameter("had_psychotherapy")));
            patientHistoryForm.setHad_psychotherapy(Boolean.parseBoolean(req.getParameter("had_psychotherapy")));
            patientHistoryForm.setHad_drug_allergy(Boolean.parseBoolean(req.getParameter("had_drug_allergies")));
            patientHistoryForm.setDrug_allergy(req.getParameter("drug_allergies_text"));

            patientHistoryFormDAO.createNewPatientHistoryForm(patientHistoryForm);

            int idx = 0;
            String family_relationship = req.getParameter("family_relationship" + idx);

            familyHistoryFormDAO.deleteFamilyHistoryFormByResident(resident.getId());

            while (family_relationship != null) {
                FamilyHistoryForm familyHistoryForm = new FamilyHistoryForm();
                familyHistoryForm.setResident_id(resident.getId());
                familyHistoryForm.setRelation(family_relationship);
                familyHistoryForm.setAge(Integer.parseInt(req.getParameter("family_member_age" + idx)));
                familyHistoryForm.setHealth_condition(req.getParameter("family_member_health_condition" + idx));
                familyHistoryForm.setIs_deceased(Boolean.parseBoolean(req.getParameter("is_deceased" + idx)));
                if (Boolean.parseBoolean(req.getParameter("is_deceased" + idx))) {
                    familyHistoryForm.setDeath_age(Integer.parseInt(req.getParameter("death_age" + idx)));
                    familyHistoryForm.setDeath_cause(req.getParameter("death_cause"+idx));
                }
                familyHistoryFormDAO.createNewFamilyHistoryForm(familyHistoryForm);

                idx++;
                family_relationship = req.getParameter("family_relationship" + idx);
            }

            resp.sendRedirect("/res.home");
        }
    }
}
