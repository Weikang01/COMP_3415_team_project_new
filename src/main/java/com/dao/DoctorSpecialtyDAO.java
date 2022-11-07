package com.dao;

import com.bean.DoctorSpecialty;

import java.util.List;

public interface DoctorSpecialtyDAO {
    List<DoctorSpecialty> getAllByDoctorId(int doctor_id);

    List<DoctorSpecialty> getAllBySpecialtyId(int specialty_id);

    void addNewSpecialty(int doctor_id, int specialty_id, int experience_level);
}
