package com.dao;

import com.bean.Symptom;

import java.util.List;

public interface SymptomDAO {
    List<Symptom> getSymptomList();

    Symptom getSymptomById(int id);
}
