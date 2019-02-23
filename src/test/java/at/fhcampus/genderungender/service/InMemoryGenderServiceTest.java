package at.fhcampus.genderungender.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import at.fhcampus.genderungender.model.GenderRequest;
import at.fhcampus.genderungender.model.GenderResult;
import at.fhcampus.genderungender.model.Word;


/**
 * Created by alex on 26.01.2019.
 */
public class InMemoryGenderServiceTest {

    private InMemoryGenderService genderService;

    private String[] texts = {
            "Verantwortlicher für Informationssicherheit (CISO) ist der abc dfg Verantwortlicher für Informationssicherheit (CISO)",
    };

    private String[] ungenderTexts = {
            "CISO",
    };



    private GenderResult[] genderResults = {
            GenderResult.builder().genderUngenderIndex(50).words(
                    Arrays.asList(
                            Word.builder()
                                    .value("Verantwortlicher für Informationssicherheit (CISO)")
                                    .options(Arrays.asList(new String[]{"CISO", "Verantwortliche bzw. Verantwortlicher für Informationssicherheit"}))
                                    .build(),
                            Word.builder()
                                    .value("ist")
                                    .build(),
                            Word.builder()
                                    .value("der/die")
                                    .build(),
                            Word.builder()
                                    .value("abc")
                                    .build(),
                            Word.builder()
                                    .value("dfg")
                                    .build(),
                            Word.builder()
                                    .value("Verantwortlicher für Informationssicherheit (CISO)")
                                    .options(Arrays.asList(new String[]{"CISO", "Verantwortliche bzw. Verantwortlicher für Informationssicherheit"}))
                                    .build()
                    )
            ).build()
    };

    private GenderResult[] ungenderResults = {
            GenderResult.builder().genderUngenderIndex(50).words(
                    Arrays.asList(
                            Word.builder()
                                    .value("CISO")
                                    .options(Arrays.asList(new String[]{"Verantwortlicher für Informationssicherheit (CISO)"}))
                                    .build()
                    )
            ).build()
    };

    @Test
    public void gender() {
        Map<String, List<String>> mockData = new HashMap<>();
        mockData.put("Verantwortlicher für Informationssicherheit (CISO)", new ArrayList<>(Arrays.asList(new String[]{
                "CISO", "Verantwortliche bzw. Verantwortlicher für Informationssicherheit"
        })));

        genderService = new InMemoryGenderService(mockData, mockData);
        for (int i = 0; i < texts.length; i++) {
            assertThat(genderService.gender(GenderRequest.builder().text(texts[i]).build(), true))
                    .isEqualTo(genderResults[i]);
        }
    }

    @Test
    public void ungender() {
        Map<String, List<String>> mockData = new HashMap<>();
        mockData.put("CISO", new ArrayList<>(Arrays.asList(new String[]{
                "Verantwortlicher für Informationssicherheit (CISO)"
        })));

        genderService = new InMemoryGenderService(mockData, mockData);
        for (int i = 0; i < ungenderTexts.length; i++) {
            assertThat(genderService.gender(GenderRequest.builder().text(ungenderTexts[i]).build(), true))
                    .isEqualTo(ungenderResults[i]);
        }
    }
}