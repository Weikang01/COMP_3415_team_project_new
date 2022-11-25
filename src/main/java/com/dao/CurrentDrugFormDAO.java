package com.dao;

import com.bean.CurrentDrugForm;

import java.util.List;

public interface CurrentDrugFormDAO {
    List<CurrentDrugForm> getCurrentDrugFormList(int resident_id);

    void createNewCurrentDrugForm(CurrentDrugForm currentDrugForm);
}
