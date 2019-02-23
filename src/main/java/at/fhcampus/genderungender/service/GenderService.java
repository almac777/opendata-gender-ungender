package at.fhcampus.genderungender.service;

import at.fhcampus.genderungender.model.GenderRequest;
import at.fhcampus.genderungender.model.GenderResult;

public interface GenderService {

    GenderResult gender(GenderRequest genderRequest, boolean includeTrigger);
    GenderResult ungender(GenderRequest genderRequest, boolean includeTrigger);
}
