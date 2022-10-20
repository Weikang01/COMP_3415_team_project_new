package com.dao;

import com.bean.Specialty;

import java.util.List;

public interface SpecialtyDAO {
    List<Specialty> getSpecialtyList();
    Specialty getSpecialtyById(int id);
}
