package com.example.teamProjectTwo.service.Impl;

import com.example.teamProjectTwo.dto.EmployeeDTO;


import com.example.teamProjectTwo.service.MyFileHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class JsonFileHandlerServiceImpl implements MyFileHandler {

    @Autowired
    KafkaController kafkaController;



    @Override
    public  void  read() throws Exception {
        // TODO: TO close resources after use
        String filename="/Users/namanbhatt/Downloads/employee.json";
        FileReader reader = new FileReader(filename);
        JSONParser jsonParser = new JSONParser();
        Object jsonData = jsonParser.parse(reader);
        JSONArray jsonArray = (JSONArray) jsonData;
        for(int i=0;i<jsonArray.size();i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String firstName = (String) jsonObject.get("firstName");
            String lastName = (String) jsonObject.get("firstName");
            Date date = parseDate((String) jsonObject.get("dateOfBirth"), "MM/dd/yy");
            long experiance = (long) jsonObject.get("experience");
            EmployeeDTO employee = new EmployeeDTO();
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setDateOfBirth(date);
            employee.setExperience(experiance);

            kafkaController.post(employee);
        }
    }



    private static  Date parseDate(String date, String format) throws ParseException,
            java.text.ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.parse(date);
    }



}
