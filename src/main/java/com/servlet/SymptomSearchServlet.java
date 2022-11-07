package com.servlet;

import com.bean.*;
import com.dao.*;
import com.dao.impl.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/symptomSearch")
public class SymptomSearchServlet extends HttpServlet {
    SymptomDAO symptomDAO = new SymptomDAOImpl();
    DiseaseSymptomDAO diseaseSymptomDAO = new DiseaseSymptomDAOImpl();
    DiseaseDAO diseaseDAO = new DiseaseDAOImpl();
    SpecialtyDiseaseDAO specialtyDiseaseDAO = new SpecialtyDiseaseDAOImpl();
    SpecialtyDAO specialtyDAO = new SpecialtyDAOImpl();
    DoctorSpecialtyDAO doctorSpecialtyDAO = new DoctorSpecialtyDAOImpl();
    DoctorDAO doctorDAO = new DoctorDAOImpl();
    HospitalDAO hospitalDAO = new HospitalDAOImpl();
    List<Symptom> symptoms = null;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (symptoms == null || symptoms.size() == 0) {
            symptoms = symptomDAO.getSymptomList();
        }

        Map<String, Symptom> value_map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        for (int i = 0; i < symptoms.size(); i++) {
            value_map.put("" + i, symptoms.get(i));
        }

        resp.setContentType("text/plain");
        PrintWriter out = resp.getWriter();
        out.write(objectMapper.writeValueAsString(value_map));
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String symptom_str = req.getParameter("symptom_str");
        String[] symptom_str_list = symptom_str.split(",");

        StringBuilder json = new StringBuilder("{");
        json.append("\"length\":\"").append(symptom_str_list.length).append("\"");
        if (symptom_str_list.length != 0) {
            json.append(",");
        }
        for (int i = 0; i < symptom_str_list.length; i++) {
            int symptom_id = symptomDAO.getIdByName(symptom_str_list[i]);

            json.append("\"").append(i)
                    .append("\":{\"id\":\"")
                    .append(symptom_id).append("\",")
                    .append("\"name\":\"")
                    .append(symptom_str_list[i])
                    .append("\",\"diseases\":{");
            List<DiseaseSymptom> allBySymptomId = diseaseSymptomDAO.getAllBySymptomId(symptom_id);

            json.append("\"length\":\"").append(allBySymptomId.size()).append("\"");
            if (allBySymptomId.size() != 0) {
                json.append(",");
            }

            for (int j = 0; j < allBySymptomId.size(); j++) {
                DiseaseSymptom diseaseSymptom = allBySymptomId.get(j);
                int disease_id = diseaseSymptom.getDisease_id();
                json
                        .append("\"").append(j)
                        .append("\":{\"id\":\"").append(disease_id)
                        .append("\",\"name\":\"")
                        .append(diseaseDAO.getDiseaseById(disease_id).getName())
                        .append("\",\"correlativity\":\"").append(diseaseSymptom.getCorrelativity())
                        .append("\",\"specialties\":{");

                List<SpecialtyDisease> allSpecialtiesByDiseaseId = specialtyDiseaseDAO.getAllSpecialtiesByDiseaseId(disease_id);

                json.append("\"length\":\"").append(allSpecialtiesByDiseaseId.size()).append("\"");
                if (allSpecialtiesByDiseaseId.size() != 0) {
                    json.append(",");
                }
                for (int k = 0; k < allSpecialtiesByDiseaseId.size(); k++) {
                    SpecialtyDisease specialtyDisease = allSpecialtiesByDiseaseId.get(k);
                    Specialty specialtyById = specialtyDAO.getSpecialtyById(specialtyDisease.getSpecialty_id());
                    List<DoctorSpecialty> allBySpecialtyId = doctorSpecialtyDAO.getAllBySpecialtyId(specialtyById.getId());
                    json.append("\"").append(k).append("\":{")
                            .append("\"id\":\"").append(specialtyById.getId())
                            .append("\",\"name\":\"").append(specialtyById.getName())
                            .append("\",\"doctors\":{");

                    json.append("\"length\":\"").append(allBySpecialtyId.size()).append("\"");
                    if (allBySpecialtyId.size() != 0) {
                        json.append(",");
                    }

                    for (int l = 0; l < allBySpecialtyId.size(); l++) {
                        Doctor doctorById = doctorDAO.getDoctorById(allBySpecialtyId.get(l).getDoctor_id());

                        json.append("\"").append(l).append("\":{\"id\":\"")
                                .append(doctorById.getId())
                                .append("\",\"experience_level\":\"")
                                .append(allBySpecialtyId.get(l).getExperience_level())
                                .append("\",\"firstname\":\"").append(doctorById.getFirstname())
                                .append("\",\"lastname\":\"").append(doctorById.getLastname())
                                .append("\",\"latitude\":\"").append(doctorById.getLatitude())
                                .append("\",\"longitude\":\"").append(doctorById.getLongitude())
                                .append("\",\"organization\":\"").append(hospitalDAO.getHospitalById(doctorById.getHospital_id()).getName())
                                .append("\"}");
                        if (l != allBySpecialtyId.size() - 1) {
                            json.append(",");
                        }
                    }

                    json.append("}");
                    if (k != allSpecialtiesByDiseaseId.size() - 1) {
                        json.append("},");
                    } else {
                        json.append("}");
                    }
                }

                json.append("}");
                if (j != allBySymptomId.size() - 1) {
                    json.append(",");
                } else {
                    json.append("}");
                }
            }

            json.append("}");
            if (i != symptom_str_list.length - 1) {
                json.append("},");
            } else {
                json.append("}");
            }
        }
        json.append("}");
        PrintWriter writer = resp.getWriter();
        writer.write(json.toString());
        writer.close();

//        System.out.println(json);
    }
}
