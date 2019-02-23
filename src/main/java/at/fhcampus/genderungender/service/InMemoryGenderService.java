package at.fhcampus.genderungender.service;

import at.fhcampus.genderungender.model.GenderRequest;
import at.fhcampus.genderungender.model.GenderResult;
import at.fhcampus.genderungender.model.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InMemoryGenderService implements GenderService {

    private static final char[] PUNCTUATION_SYMBOLS = {'.', ',', '!', '?', ';', '-'};

    private final Map<String, List<String>> genderDictionary;
    private final Map<String, List<String>> ungenderDictionary;

    @Override
    public GenderResult gender(GenderRequest genderRequest, boolean includeTrigger) {
        Map<String, List<String>> wordsToSubstitute = genderDictionary.entrySet().stream()
                .filter(entry -> genderRequest.getText().contains(entry.getKey()))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        int longestWord = getLongestWordBlockLength(wordsToSubstitute);

        String genderRequestText = genderRequest.getText();

        for (char PUNCTUATION : PUNCTUATION_SYMBOLS) {
            genderRequestText = genderRequestText.replace(PUNCTUATION + "", " " + PUNCTUATION);
        }
        genderRequestText = genderRequestText.replaceAll(" der ", ".1.");
        genderRequestText = genderRequestText.replaceAll(" die ", ".1.");
        genderRequestText = genderRequestText.replaceAll(".1.", " der/die ");

        List<String> words = new ArrayList<>(Arrays.asList(genderRequestText.split(" ")));

        List<Word> text = new ArrayList<>();
        extractWords(wordsToSubstitute, longestWord, words, text);

        if (includeTrigger) {
            return GenderResult.builder()
                    .words(text)
                    .genderUngenderIndex(calculateTriggerValue(genderRequest))
                    .build();
        } else {
            return GenderResult.builder()
                    .words(text)
                    .build();
        }

    }

    private String getWordToCompare(List<String> words, int length) {
        length++;
        String[] out = new String[length];
        for (int i = 0; i < length; i++) {
            out[i] = words.get(i);
        }
        return Arrays.stream(out).collect(Collectors.joining(" "));
    }

    private Integer getLongestWordBlockLength(Map<String, List<String>> wordsToSubstitute) {
        return wordsToSubstitute.keySet().stream().map(word -> word.split(" ")).map(list -> list.length)
                .max(Integer::compareTo).orElse(4);
    }

    private int calculateTriggerValue(GenderRequest request) {
        GenderResult genderResult = gender(request, false);
        GenderResult ungenderResult = ungender(request, false);

        double ungendered = new Long(genderResult.getWords().stream().filter(word -> !CollectionUtils.isEmpty(word.getOptions())).count()).doubleValue();
        double gendered = new Long(ungenderResult.getWords().stream().filter(word -> !CollectionUtils.isEmpty(word.getOptions())).count()).doubleValue();

        double total = gendered + ungendered;

        return (int) (ungendered / total * 100);
    }


    @Override
    public GenderResult ungender(GenderRequest genderRequest, boolean includeTrigger) {
        int longestWord = getLongestWordBlockLength(ungenderDictionary);

        String genderRequestText = genderRequest.getText();

        for (char PUNCTUATION : PUNCTUATION_SYMBOLS) {
            genderRequestText = genderRequestText.replace(PUNCTUATION + "", " " + PUNCTUATION);
        }

        List<String> words = new ArrayList<>(Arrays.asList(genderRequestText.split(" ")));

        List<Word> text = new ArrayList<>();
        extractWords(ungenderDictionary, longestWord, words, text);

        if (includeTrigger) {
            return GenderResult.builder()
                    .words(text)
                    .genderUngenderIndex(calculateTriggerValue(genderRequest))
                    .build();
        } else {
            return GenderResult.builder()
                    .words(text)
                    .build();
        }
    }

    private void extractWords(Map<String, List<String>> flatMap, int longestWord, List<String> words, List<Word> text) {
        while (!words.isEmpty()) {
            boolean foundWord = false;
            for (int i = 0; i < longestWord && i < words.size(); i++) {
                String toCompare = getWordToCompare(words, i);
                boolean contains = flatMap.containsKey(toCompare);
                if (contains) {
                    text.add(Word.builder()
                            .value(toCompare)
                            .options(flatMap.get(toCompare))
                            .build());
                    for (int idxToRemove = 0; idxToRemove <= i; idxToRemove++) {
                        words.remove(0);
                    }
                    foundWord = true;
                    break;
                }
            }
            if (!foundWord) {
                String removed = words.remove(0);
                text.add(Word.builder().value(removed).build());
            }
        }
    }
}
