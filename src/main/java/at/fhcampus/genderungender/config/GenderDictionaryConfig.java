package at.fhcampus.genderungender.config;

import at.fhcampus.genderungender.util.GenderInputReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class GenderDictionaryConfig {

    @Bean
    public Map<String, List<String>> genderDictionary(GenderInputReader inputReader){
        return inputReader.readCsv("worttabelle.csv");
    }

    @Bean
    public Map<String, List<String>> ungenderDictionary(GenderInputReader inputReader) {
        Map<String, List<String>> map = inputReader.readCsv("worttabelle.csv");

        Map<String, List<String>> flatMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            for (String value : entry.getValue()) {
                ArrayList<String> innerList = new ArrayList<String>();
                innerList.add(entry.getKey());
                flatMap.put(value, innerList);
            }
        }
        return flatMap;
    }
}
