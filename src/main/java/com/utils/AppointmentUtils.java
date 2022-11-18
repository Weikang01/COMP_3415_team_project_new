package com.utils;
import com.bean.Appointment;
import com.bean.AppointmentStatus;
import com.bean.Doctor;
import com.bean.Resident;
import com.dao.DoctorDAO;
import com.dao.ResidentDAO;
import com.dao.impl.DoctorDAOImpl;
import com.dao.impl.ResidentDAOImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentUtils {
    private static final DoctorDAO doctorDAO = new DoctorDAOImpl();
    private static final ResidentDAO residentDAO = new ResidentDAOImpl();

    public static String appointmentListJson(List<Appointment> appointmentList) {
        StringBuilder json = new StringBuilder("{").append("\"length\": ").append(appointmentList.size());
        if (appointmentList.size()>0)
            json.append(", ");

        for (int i = 0; i < appointmentList.size(); i++) {
            json.append("\"").append(i).append("\":{ \"id\":").append(appointmentList.get(i).getId()).append(", \"resident\":{");

            Resident resident = residentDAO.getResidentById(appointmentList.get(i).getResident_id());
            json.append("\"id\":").append(resident.getId())
                    .append(",\"firstname\":\"").append(resident.getFirstname()).append("\"")
                    .append(",\"lastname\":\"").append(resident.getLastname()).append("\"")
                    .append(",\"address\":\"").append(resident.getAddress()).append("\"")
                    .append(",\"latitude\":\"").append(resident.getLatitude()).append("\"")
                    .append(",\"longitude\":\"").append(resident.getLongitude()).append("\"")
                    .append("},");
            Doctor doctor = doctorDAO.getDoctorById(appointmentList.get(i).getDoctor_id());
            json.append("\"doctor\":{");
            json.append("\"id\":").append(doctor.getId())
                    .append(",\"firstname\":\"").append(doctor.getFirstname()).append("\"")
                    .append(",\"lastname\":\"").append(doctor.getLastname()).append("\"")
                    .append(",\"address\":\"").append(doctor.getAddress()).append("\"")
                    .append(",\"latitude\":\"").append(doctor.getLatitude()).append("\"")
                    .append(",\"longitude\":\"").append(doctor.getLongitude()).append("\"")
                    .append("},");
            DateFormat df = new SimpleDateFormat("MMddyyyy");
            String result = df.format(appointmentList.get(i).getDate());
            json.append("\"month\":").append(Integer.parseInt(result.substring(0,2))).append(",");
            json.append("\"day\":").append(Integer.parseInt(result.substring(2,4))).append(",");
            json.append("\"year\":").append(Integer.parseInt(result.substring(4))).append(",");
            json.append("\"hour\":").append(appointmentList.get(i).getHour()).append(",");
            json.append("\"min\":").append(appointmentList.get(i).getMin()).append(",");
            json.append("\"status\":").append(appointmentList.get(i).getStatus());

            json.append("}");
            if (i != appointmentList.size() - 1) {
                json.append(", ");
            }
        }
        json.append("}");
        return json.toString();
    }
}
