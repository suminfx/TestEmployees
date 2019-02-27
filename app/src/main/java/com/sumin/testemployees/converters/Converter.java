package com.sumin.testemployees.converters;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.util.Log;

import com.google.gson.Gson;
import com.sumin.testemployees.pojo.Speciality;

import java.util.ArrayList;
import java.util.List;


public class Converter {
    @TypeConverter
    public List<Speciality> jsonAsStringToListSpeciality(String jsonAsString) {
        Gson gson = new Gson();
        ArrayList result = new Gson().fromJson(jsonAsString, ArrayList.class);
        List<Speciality> specialities = new ArrayList<>();
        for (Object s : result) {
            specialities.add(gson.fromJson(s.toString(), Speciality.class));
        }
        return specialities;
    }

    @TypeConverter
    public String specialitiesListToString(List<Speciality> specialities) {
        return new Gson().toJson(specialities);
    }
}
