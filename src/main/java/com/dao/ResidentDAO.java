package com.dao;

import com.bean.Resident;

import java.util.List;

/**
 * @Description: ResidentDAO
 * @author: Weikang
 * @date: 2022/9/29 12:04
 */

public interface ResidentDAO{
    void insert(Resident resident);

    void delete(int id);

    void delete(String username);

    void update(Resident resident);

    Resident getResidentById(int id);

    String getUsernameById(int id);

    int getIdByUsername(String username);

    Resident getResidentByUsername(String username);

    boolean isUsernameExist(String username);

    boolean checkLoginInfo(String username, String password);

    List<Resident> getResidentsNearCoord(double latitude, double longitude, double max_distance);
}
