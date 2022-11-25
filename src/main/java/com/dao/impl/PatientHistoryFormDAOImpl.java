package com.dao.impl;

import com.bean.PatientHistoryForm;
import com.dao.BaseDAO;
import com.dao.PatientHistoryFormDAO;

import java.util.List;

public class PatientHistoryFormDAOImpl extends BaseDAO implements PatientHistoryFormDAO {
    @Override
    public List<PatientHistoryForm> getPatientHistoryFormList(int resident_id) {
        String sql = "select * from patient_history_form where resident_id=?";
        return getInstanceList(PatientHistoryForm.class, sql, resident_id);
    }

    @Override
    public void createNewPatientHistoryForm(PatientHistoryForm patientHistoryForm) {
        String sql = "insert into patient_history_form " +
                "(`resident_id`, `description`, `had_ECT`, `had_psychotherapy`, `had_drug_allergy`, `drug_allergy`) values " +
                "(            ?,             ?,         ?,                   ?,                  ?,              ?)";
        update(sql, patientHistoryForm.getResident_id(), patientHistoryForm.getDescription(),
                patientHistoryForm.isHad_ECT(), patientHistoryForm.isHad_psychotherapy(),
                patientHistoryForm.isHad_drug_allergy(), patientHistoryForm.getDrug_allergy());
    }

    @Override
    public PatientHistoryForm getMostRecentHistoryForm(int resident_id) {
        String sql = "select * from patient_history_form t1 where (t1.create_date = " +
                "(select max(t2.create_date)" +
                " from patient_history_form t2" +
                " where t2.id = t1.id)) and t1.resident_id=?";
        return getInstance(PatientHistoryForm.class, sql, resident_id);
    }
}
