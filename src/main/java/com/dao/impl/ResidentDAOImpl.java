package com.dao.impl;

import com.bean.Resident;
import com.dao.BaseDAO;
import com.dao.ResidentDAO;

import java.util.List;

/**
 * @Description: ResidentDAOImpl
 * @author: Weikang
 * @version: 1.0
 * @date: 29/09/2022 12:23
 */

public class ResidentDAOImpl extends BaseDAO implements ResidentDAO {

    @Override
    public void insert(Resident resident) {
        String sql = "insert into residents " +
                "(username, password, firstname, lastname, gender, address, latitude, longitude, tel, birthdate) values " +
                "(?,        ?,        ?,         ?,        ?,      ?,       ?,        ?,         ?,   ?)";
        update(sql, resident.getUsername(), resident.getPassword(), resident.getFirstname(), resident.getLastname(),
                resident.getGender(), resident.getAddress(), resident.getLatitude(), resident.getLongitude(), resident.getTel(),
                resident.getBirthdate());
    }

    @Override
    public void delete(int id) {
        String sql = "update residents set is_active = 0 where id = ?";
        update(sql, id);
    }

    @Override
    public void delete(String username) {
        String sql = "update residents set is_active = 0 where username = ?";
        update(sql, username);
    }

    @Override
    public void update(Resident resident) {
        String sql = "update residents set " +
                "password=?, firstname=?, lastname=?, gender=?, address=?, latitude=?, longitude=?, tel=?, birthdate=? where " +
                "username=?";
        update(sql, resident.getPassword(), resident.getFirstname(), resident.getLastname(),
                resident.getGender(), resident.getAddress(), resident.getLatitude(),
                resident.getLongitude(), resident.getBirthdate(), resident.getUsername());
    }

    @Override
    public Resident getResidentById(int id) {
        String sql = "select * from residents where id=?";
        return getInstance(Resident.class, sql, id);
    }

    @Override
    public String getUsernameById(int id) {
        String sql = "select username from residents where id=?";
        return getInstance(Resident.class, sql, id).getUsername();
    }

    @Override
    public int getIdByUsername(String username) {
        String sql = "select id from residents where username=?";
        return getInstance(Resident.class, sql, username).getId();
    }

    @Override
    public Resident getResidentByUsername(String username) {
        String sql = "select * from residents where username=?";
        return getInstance(Resident.class, sql, username);
    }

    @Override
    public boolean isUsernameExist(String username) {
        String sql = "select username from residents where username=?";
        return getInstance(Resident.class, sql, username) != null;
    }

    @Override
    public boolean checkLoginInfo(String username, String password) {
        String sql = "select username from residents where username=? and password=?";
        return getInstance(Resident.class, sql, username, password) != null;
    }

    @Override
    public List<Resident> getResidentsNearCoord(double latitude, double longitude, double max_distance) {
        String sql = "SELECT  \n" +
                "  *,(  \n" +
                "    6371 * acos (  \n" +
                "      cos ( radians( ? ) )  \n" +
                "      * cos( radians( latitude ) )  \n" +
                "      * cos( radians( longitude ) - radians( ? ) )  \n" +
                "      + sin ( radians( ? ) )  \n" +
                "      * sin( radians( latitude ) )  \n" +
                "    )  \n" +
                "  ) AS distance\n" +
                "  FROM residents\n" +
                "  HAVING distance < ?" +
                "  ORDER BY distance;";
        return getInstanceList(Resident.class, sql, latitude, longitude, latitude, max_distance);
    }
}
