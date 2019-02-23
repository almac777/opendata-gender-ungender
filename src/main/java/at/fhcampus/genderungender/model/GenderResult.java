package at.fhcampus.genderungender.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenderResult {
    private List<Word> words;
    private int genderUngenderIndex;
}
