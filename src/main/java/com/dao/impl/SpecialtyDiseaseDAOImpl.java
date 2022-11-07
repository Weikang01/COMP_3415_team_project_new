package com.dao.impl;

import com.bean.SpecialtyDisease;
import com.dao.BaseDAO;
import com.dao.SpecialtyDiseaseDAO;

import java.util.List;

public class SpecialtyDiseaseDAOImpl extends BaseDAO implements SpecialtyDiseaseDAO {
    @Override
    public List<SpecialtyDisease> getAllDiseasesBySpecialtyId(int specialty_id) {
        String sql = "select * from specialty_disease where specialty_id=?";
        return super.getInstanceList(SpecialtyDisease.class, sql, specialty_id);
    }

    @Override
    public List<SpecialtyDisease> getAllSpecialtiesByDiseaseId(int disease_id) {
        String sql = "select * from specialty_disease where disease_id=?";
        return super.getInstanceList(SpecialtyDisease.class, sql, disease_id);
    }
}
