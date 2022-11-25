package com.dao.impl;

import com.bean.FamilyHistoryForm;
import com.dao.BaseDAO;
import com.dao.FamilyHistoryFormDAO;

import java.util.List;

public class FamilyHistoryFormDAOImpl extends BaseDAO implements FamilyHistoryFormDAO {
    @Override
    public List<FamilyHistoryForm> getFamilyHistoryFormList(int resident_id) {
        String sql = "select * from family_history_form where resident_id=?";
        return getInstanceList(FamilyHistoryForm.class, sql, resident_id);
    }

    @Override
    public void createNewFamilyHistoryForm(FamilyHistoryForm familyHistoryForm) {
        String sql = "insert into family_history_form " +
                "(`resident_id`, `relation`, `age`, `health_condition`, `is_deceased`, `death_age`, `death_cause`) values " +
                "(            ?,          ?,     ?,                  ?,             ?,           ?,             ?)";
        update(sql, familyHistoryForm.getResident_id(), familyHistoryForm.getRelation(),
                familyHistoryForm.getAge(), familyHistoryForm.getHealth_condition(), familyHistoryForm.isIs_deceased(),
                familyHistoryForm.getDeath_age(), familyHistoryForm.getDeath_cause());
    }

    @Override
    public void deleteFamilyHistoryFormByResident(int resident_id) {
        String sql = "delete from family_history_form where resident_id=?";
        update(sql, resident_id);
    }
}
