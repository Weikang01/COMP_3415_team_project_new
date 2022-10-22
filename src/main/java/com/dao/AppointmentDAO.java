package com.dao;

import com.bean.Appointment;

import java.util.List;

public interface AppointmentDAO {
    void insert(Appointment appointment);

    void update(Appointment appointment);

    void delete(int id);

    void deleteAllByResidentId(int resident_id);

    void deleteAllByDoctorId(int doctor_id);

    List<Appointment> getAppointmentListByResidentId(int resident_id);

    List<Appointment> getAppointmentListByDoctorId(int doctor_id);

    List<Appointment> getAppointmentListByResidentAndDoctor(int resident_id, int doctor_id);
}
