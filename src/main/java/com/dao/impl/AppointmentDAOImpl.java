package com.dao.impl;

import com.bean.Appointment;
import com.dao.AppointmentDAO;
import com.dao.BaseDAO;

import java.util.List;

public class AppointmentDAOImpl extends BaseDAO implements AppointmentDAO {
    @Override
    public void insert(Appointment appointment) {
        String sql = "insert into appointments (resident_id, doctor_id, date, hour, min) values " +
                                              "(?,           ?,         ?,    ?,    ?)";
        update(sql, appointment.getResident_id(), appointment.getDoctor_id(),
                appointment.getDate(), appointment.getHour(), appointment.getMin());
    }

    @Override
    public void update(Appointment appointment) {
        String sql = "update appointments set " +
                "resident_id=?, doctor_id=?, date=?, hour=?, min=? where " +
                "id=?";
        update(sql, appointment.getResident_id(), appointment.getDoctor_id(),
                appointment.getDate(), appointment.getHour(), appointment.getMin(),
                appointment.getId());
    }

    @Override
    public void delete(int id) {
        String sql = "delete from appointments where id=?";
        super.update(sql, id);
    }

    @Override
    public void deleteAllByResidentId(int resident_id) {
        String sql = "delete from appointments where resident_id=?";
        super.update(sql, resident_id);
    }

    @Override
    public void deleteAllByDoctorId(int doctor_id) {
        String sql = "delete from appointments where doctor_id=?";
        super.update(sql, doctor_id);
    }

    @Override
    public List<Appointment> getAppointmentListByResidentId(int resident_id) {
        String sql = "select * where resident_id=?";
        return getInstanceList(Appointment.class, sql, resident_id);
    }

    @Override
    public List<Appointment> getAppointmentListByDoctorId(int doctor_id) {
        String sql = "select * where doctor_id=?";
        return getInstanceList(Appointment.class, sql, doctor_id);
    }

    @Override
    public List<Appointment> getAppointmentListByResidentAndDoctor(int resident_id, int doctor_id) {
        String sql = "select * where resident_id=? and doctor_id=?";
        return getInstanceList(Appointment.class, sql, resident_id, doctor_id);
    }
}
