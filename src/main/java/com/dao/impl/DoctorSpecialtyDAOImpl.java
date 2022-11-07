package com.dao.impl;

import com.bean.DoctorSpecialty;
import com.dao.BaseDAO;
import com.dao.DoctorSpecialtyDAO;

import java.util.List;

public class DoctorSpecialtyDAOImpl extends BaseDAO implements DoctorSpecialtyDAO {
    @Override
    public List<DoctorSpecialty> getAllByDoctorId(int doctor_id) {
        String sql = "select * from doctor_specialty where doctor_id=?";
        return super.getInstanceList(DoctorSpecialty.class, sql, doctor_id);
    }

    @Override
    public List<DoctorSpecialty> getAllBySpecialtyId(int specialty_id) {
        String sql = "select * from doctor_specialty where specialty_id=?";
        return super.getInstanceList(DoctorSpecialty.class, sql, specialty_id);
    }

    @Override
    public void addNewSpecialty(int doctor_id, int specialty_id, int experience_level) {
        String sql = "insert into doctor_specialty (doctor_id, specialty_id, experience_level) values (?,?,?)";
        super.update(sql, doctor_id, specialty_id, experience_level);
    }
}
