package com.dao;

import com.bean.Hospital;

import java.util.List;

public interface HospitalDAO{
    List<Hospital> getHospitalList();

    Hospital getHospitalById(int id);
}
