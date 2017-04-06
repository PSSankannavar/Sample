package com.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
 
import org.json.simple.JSONArray;
import com.gemstone.org.json.JSONObject;
import org.json.simple.parser.JSONParser;
 
/**
 * @author Crunchify.com
 */
 
public class CrunchifyJSONReadFromFile {
 
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
 
        try {
			InputStream inputStreamObject = CrunchifyJSONReadFromFile.class
					.getResourceAsStream("/com/mock/EstartMock.json");

			BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStreamObject, "UTF-8"));
			StringBuilder responseStrBuilder = new StringBuilder();
			String inputStr;
			while ((inputStr = streamReader.readLine()) != null)
				responseStrBuilder.append(inputStr);

			JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
        	
             
             //Object obj = parser.parse(new FileReader(input.));
             System.out.println(jsonObject);
 
            String name = (String) jsonObject.get("Name");
            String author = (String) jsonObject.get("Author");
            JSONArray companyList = (JSONArray) jsonObject.get("Company List");
 
            System.out.println("Name: " + name);
            System.out.println("Author: " + author);
            System.out.println("\nCompany List:");
            Iterator<String> iterator = companyList.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}