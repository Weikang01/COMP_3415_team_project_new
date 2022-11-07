package com.dao.impl;

import com.bean.DiseaseSymptom;
import com.dao.BaseDAO;
import com.dao.DiseaseSymptomDAO;

import java.util.List;

public class DiseaseSymptomDAOImpl extends BaseDAO implements DiseaseSymptomDAO {
    @Override
    public List<DiseaseSymptom> getAllBySymptomId(int symptom_id) {
        String sql = "select * from disease_symptom where symptom_id=?";
        return super.getInstanceList(DiseaseSymptom.class, sql, symptom_id);
    }

    @Override
    public List<DiseaseSymptom> getAllByDiseaseId(int disease_id) {
        String sql = "select * from disease_symptom where disease_id=?";
        return super.getInstanceList(DiseaseSymptom.class, sql, disease_id);
    }
}
