package com.dao.impl;

import com.bean.Specialty;
import com.dao.BaseDAO;
import com.dao.SpecialtyDAO;

import java.util.List;

public class SpecialtyDAOImpl extends BaseDAO implements SpecialtyDAO {
    @Override
    public List<Specialty> getSpecialtyList() {
        String sql = "select * from specialties";
        return super.getInstanceList(Specialty.class, sql);
    }

    @Override
    public Specialty getSpecialtyById(int id) {
        String sql = "select * from specialties where id=?";
        return super.getInstance(Specialty.class, sql, id);
    }
}
