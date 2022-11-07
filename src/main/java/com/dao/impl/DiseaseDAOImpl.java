package com.dao.impl;

import com.bean.Disease;
import com.dao.BaseDAO;
import com.dao.DiseaseDAO;

public class DiseaseDAOImpl extends BaseDAO implements DiseaseDAO {
    @Override
    public Disease getDiseaseById(int id) {
        String sql = "select * from diseases where id=?";
        return super.getInstance(Disease.class, sql, id);
    }
}
