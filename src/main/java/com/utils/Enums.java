package com.utils;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Enums {
    private static final String[] genders = new String[]{"male", "female", "other", "prefer not to say"};

    public static int genderLength() {
        return genders.length;
    }

    public static String getGender(int i) {
        return genders[i];
    }

    public static int getGenderIndex(String gen) {
        int i = 0;
        for (; i < genders.length; i++) {
            if (genders[i].equals(gen))
                return i;
        }
        throw new NoSuchElementException();
    }

    public static String[] getGenders() {
        return genders;
    }
}
