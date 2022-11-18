package com.bean;

import java.util.Arrays;
import java.util.Optional;

public enum AppointmentStatus {
    pending(0),
    confirmed(1),
    cancelled(2),
    rescheduling(3),
    transferring(4);
    private final int value;
    private AppointmentStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Optional<AppointmentStatus> getAppointmentStatus(int index) {
        return Arrays.stream(values())
                .filter(AppointmentStatus -> AppointmentStatus.value == index)
                .findFirst();
    }

    public static String getAppointmentStatusName(int index) {
        if (getAppointmentStatus(index).isPresent()) {
            return getAppointmentStatus(index).get().name();
        } else return "";
    }

    public static boolean isIndexExists(int index) {
        for (AppointmentStatus status: AppointmentStatus.values()) {
            if (status.getValue() == index) {
                return true;
            }
        }
        return false;
    }

}
