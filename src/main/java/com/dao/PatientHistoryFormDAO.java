package com.dao;

import com.bean.PatientHistoryForm;

import java.util.List;

public interface PatientHistoryFormDAO {
    List<PatientHistoryForm> getPatientHistoryFormList(int resident_id);

    void createNewPatientHistoryForm(PatientHistoryForm patientHistoryForm);

    PatientHistoryForm getMostRecentHistoryForm(int resident_id);
}
