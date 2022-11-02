package com.dao.impl;

import com.bean.Symptom;
import com.dao.BaseDAO;
import com.dao.SymptomDAO;

import java.util.List;

public class SymptomDAOImpl extends BaseDAO implements SymptomDAO {
    @Override
    public List<Symptom> getSymptomList() {
        String sql = "select * from symptoms";
        return getInstanceList(Symptom.class, sql);
    }

    @Override
    public Symptom getSymptomById(int id) {
        String sql = "select * from symptoms where id=?";
        return getInstance(Symptom.class, sql, id);
    }
}
