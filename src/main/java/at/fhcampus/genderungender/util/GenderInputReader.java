package at.fhcampus.genderungender.util;

import org.springframework.stereotype.Component;


import java.io.*;
import java.util.*;

@Component
public class GenderInputReader {

    BufferedReader bufferedReader;
    Map<String, List<String>> response;
    String line;


    public Map<String, List<String>> readCsv(String fileName) {
        response = new HashMap<>();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try {
            InputStream inputStream = new FileInputStream(file);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    String[] splitLine = line.split(";");
                    String key = splitLine[1];
                    String value = splitLine[2];
                    if (response.containsKey(key))
                    {
                        List<String> values = response.get(key);
                        values.add(value);
                        response.put(key, values);
                    }
                    else
                    {
                        List<String> values = new ArrayList<>();
                        values.add(value);
                        response.put(key, values);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}

