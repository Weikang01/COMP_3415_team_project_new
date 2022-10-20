package com.dao.impl;

import com.bean.Hospital;
import com.dao.BaseDAO;
import com.dao.HospitalDAO;

import java.util.List;

public class HospitalDAOImpl extends BaseDAO implements HospitalDAO {
    @Override
    public List<Hospital> getHospitalList() {
        String sql = "select * from hospitals";
        return super.getInstanceList(Hospital.class, sql);
    }

    @Override
    public Hospital getHospitalById(int id) {
        String sql = "select * from hospitals where id=?";
        return super.getInstance(Hospital.class, sql, id);
    }
}
