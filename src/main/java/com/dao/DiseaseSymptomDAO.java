package com.dao;

import com.bean.DiseaseSymptom;

import java.util.List;

public interface DiseaseSymptomDAO {
    List<DiseaseSymptom> getAllBySymptomId(int symptom_id);

    List<DiseaseSymptom> getAllByDiseaseId(int disease_id);
}
