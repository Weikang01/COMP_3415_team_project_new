package com.dao.impl;

import com.bean.CurrentDrugForm;
import com.dao.BaseDAO;
import com.dao.CurrentDrugFormDAO;

import java.util.List;

public class CurrentDrugFormDAOImpl extends BaseDAO implements CurrentDrugFormDAO {
    @Override
    public List<CurrentDrugForm> getCurrentDrugFormList(int resident_id) {
        String sql = "select * from drug_allergy_form where resident_id=?";
        return getInstanceList(CurrentDrugForm.class, sql, resident_id);
    }

    @Override
    public void createNewCurrentDrugForm(CurrentDrugForm currentDrugForm) {
        String sql = "insert into drug_allergy_form " +
                "(`resident_id`, `name`, `dose`, `how_long`) values " +
                "(            ?,      ?,      ?,          ?)";
        update(sql, currentDrugForm.getResident_id(), currentDrugForm.getName(), currentDrugForm.getDose(), currentDrugForm.getHow_long());
    }
}
