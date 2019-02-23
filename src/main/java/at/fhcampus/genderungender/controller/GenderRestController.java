package at.fhcampus.genderungender.controller;


import at.fhcampus.genderungender.model.GenderRequest;
import at.fhcampus.genderungender.model.GenderResult;
import at.fhcampus.genderungender.service.InMemoryGenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class GenderRestController {

    private final InMemoryGenderService genderService;

    @PostMapping("/gender")
    public GenderResult gender(@RequestBody GenderRequest request){
        return genderService.gender(request, true);
    }

    @PostMapping("/ungender")
    public GenderResult ungender(@RequestBody GenderRequest request){
        return genderService.ungender(request, true);
    }
}
