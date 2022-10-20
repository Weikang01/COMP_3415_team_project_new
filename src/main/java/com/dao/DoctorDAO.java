package com.dao;

import com.bean.Doctor;

import java.util.List;

/**
* @Description: DoctorDAO
* @author: Weikang
* @date: 2022/9/29 12:05
*/

public interface DoctorDAO {
    void insert(Doctor mp);

    void delete(int id);

    void delete(String username);

    void update(Doctor mp);

    Doctor getDoctorById(int id);

    Doctor getDoctorByUsername(String username);

    boolean isUsernameExist(String username);

    boolean checkLoginInfo(String username, String password);

    List<Doctor> getDoctorsNearCoord(double latitude, double longitude, double max_distance);
}
