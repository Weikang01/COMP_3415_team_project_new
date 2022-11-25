package com.dao;

import com.bean.FamilyHistoryForm;

import java.util.List;

public interface FamilyHistoryFormDAO {
    List<FamilyHistoryForm> getFamilyHistoryFormList(int resident_id);

    void createNewFamilyHistoryForm(FamilyHistoryForm familyHistoryForm);

    void deleteFamilyHistoryFormByResident(int resident_id);
}
