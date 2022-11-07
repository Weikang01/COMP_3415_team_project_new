package com.utils;

import com.bean.Doctor;
import com.bean.DoctorSpecialty;
import com.dao.DoctorDAO;
import com.dao.DoctorSpecialtyDAO;
import com.dao.HospitalDAO;
import com.dao.SpecialtyDAO;
import com.dao.impl.DoctorDAOImpl;
import com.dao.impl.DoctorSpecialtyDAOImpl;
import com.dao.impl.HospitalDAOImpl;
import com.dao.impl.SpecialtyDAOImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class DoctorUtils {
    private static final DoctorDAO doctorDAO = new DoctorDAOImpl();
    private static final DoctorSpecialtyDAO doctorSpecialtyDAO = new DoctorSpecialtyDAOImpl();
    private static final HospitalDAO hospitalDAO = new HospitalDAOImpl();
    private static final SpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();

    public static String doctorListJson(List<Doctor> doctorList) {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder doctorJson = new StringBuilder("{");
        doctorJson.append("\"length\": ").append(doctorList.size()).append(", ");
        for (int i = 0; i < doctorList.size(); i++) {
            doctorJson.append("\"").append(i).append("\":");
            doctorList.get(i).setHospital(hospitalDAO.getHospitalById(doctorList.get(i).getHospital_id()).getName());
            List<DoctorSpecialty> allByDoctorId = doctorSpecialtyDAO.getAllByDoctorId(doctorList.get(i).getId());
            for (DoctorSpecialty doctorSpecialty : allByDoctorId) {
                doctorList.get(i).addSpecialty(specialtyDAO.getSpecialtyById(doctorSpecialty.getSpecialty_id()).getName());
            }
            try {
                doctorJson.append(mapper.writeValueAsString(doctorList.get(i)));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (i != doctorList.size() - 1) {
                doctorJson.append(",");
            }
        }
        doctorJson.append("}");
        return doctorJson.toString();
    }
}
