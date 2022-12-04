package com.dao.impl;

import com.bean.Doctor;
import com.dao.BaseDAO;
import com.dao.DoctorDAO;
import com.dao.HospitalDAO;
import com.dao.SpecialtyDAO;

import java.util.List;

public class DoctorDAOImpl extends BaseDAO implements DoctorDAO {
    HospitalDAO hospitalDAO = new HospitalDAOImpl();
    SpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    
    @Override
    public void insert(Doctor doctor) {
        String sql = "insert into doctors " +
                "(username, password, firstname, lastname, address, latitude, longitude, tel, hospital_id) values " +
                "(?,        ?,        ?,         ?,        ?,       ?,        ?,         ?,   ?          )";
        update(sql, doctor.getUsername(), doctor.getPassword(), doctor.getFirstname(), doctor.getLastname(),
                doctor.getAddress(), doctor.getLatitude(), doctor.getLongitude(), doctor.getTel(), doctor.getHospital_id());
    }

    @Override
    public void delete(int id) {
        String sql = "update doctors set is_active = 0 where id = ?";
        update(sql, id);
    }

    @Override
    public void delete(String username) {
        String sql = "update doctors set is_active = 0 where username = ?";
        update(sql, username);
    }

    @Override
    public void update(Doctor doctor) {
        String sql = "update doctors set " +
                "password=?, firstname=?, lastname=?, address=?, latitude=?, longitude=?, tel=?, " +
                "hospital_id=?, where " +
                "username=?";
        update(sql, doctor.getPassword(), doctor.getFirstname(), doctor.getLastname(),
                doctor.getAddress(), doctor.getLatitude(), doctor.getLongitude(),
                doctor.getTel(), doctor.getHospital_id(),
                doctor.getUsername());
    }
    
    @Override
    public Doctor getDoctorById(int id) {
        String sql = "select * from doctors where id=?";
        Doctor doctor = getInstance(Doctor.class, sql, id);
        doctor.setHospital(hospitalDAO.getHospitalById(doctor.getHospital_id()).getName());

        return doctor;
    }

    @Override
    public Doctor getDoctorByUsername(String username) {
        String sql = "select * from doctors where username=?";
        Doctor doctor = getInstance(Doctor.class, sql, username);
        doctor.setHospital(hospitalDAO.getHospitalById(doctor.getHospital_id()).getName());

        return doctor;
    }

    @Override
    public boolean isUsernameExist(String username) {
        String sql = "select username from doctors where username=?";
        return getInstance(Doctor.class, sql, username) != null;
    }

    @Override
    public boolean checkLoginInfo(String username, String password) {
        String sql = "select username from doctors where username=? and password=?";
        return getInstance(Doctor.class, sql, username, password) != null;
    }

    @Override
    public List<Doctor> getDoctorsNearCoord(double latitude, double longitude, double max_distance) {
        String sql =
                "SELECT *, \n" +
                "ROUND(\n" +
                "6378.138 * 2 * ASIN(\n" +
                "SQRT(\n" +
                "POW( SIN( ( ? * PI( ) \n" +
                "/ 180 - latitude * PI( ) / 180 ) / 2 ), 2 ) \n" +
                "+ COS( ? * PI( ) / 180 ) \n" +
                "* COS( latitude * PI( ) / 180 ) \n" +
                "* POW( SIN( ( ? * PI( ) \n" +
                "/ 180 - longitude * PI( ) / 180 ) / 2 ), 2 ) \n" +
                ") \n" +
                ") \n" +
                ") AS distance" +
                "  FROM doctors\n" +
                "  HAVING distance < ?" +
                "  ORDER BY distance";
        List<Doctor> doctorList = getInstanceList(Doctor.class, sql, latitude, latitude, longitude, max_distance);

        for (Doctor doctor :
                doctorList) {
            doctor.setHospital(hospitalDAO.getHospitalById(doctor.getHospital_id()).getName());
        }
        
        return doctorList;
    }
}
