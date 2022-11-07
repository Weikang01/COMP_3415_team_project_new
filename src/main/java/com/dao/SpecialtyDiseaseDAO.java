package com.dao;

import com.bean.SpecialtyDisease;

import java.util.List;

public interface SpecialtyDiseaseDAO {
    public List<SpecialtyDisease> getAllDiseasesBySpecialtyId(int specialty_id);

    public List<SpecialtyDisease> getAllSpecialtiesByDiseaseId(int disease_id);
}
