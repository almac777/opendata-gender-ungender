package at.fhcampus.genderungender.controller;

import at.fhcampus.genderungender.model.DownloadRequest;
import at.fhcampus.genderungender.model.GenderRequest;
import at.fhcampus.genderungender.model.GenderResult;
import at.fhcampus.genderungender.model.Word;
import at.fhcampus.genderungender.service.InMemoryGenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@Controller
@RequiredArgsConstructor
public class GenderController {

    public static final String INDEX_VIEW = "index";
    public static final String GENDERED_RESULT_VIEW = "gendered_result";

    private final InMemoryGenderService service;

    @GetMapping
    private ModelAndView indexPage(){
        ModelAndView modelAndView = new ModelAndView();
        GenderRequest genderRequest = new GenderRequest();
        genderRequest.setType(1);
        modelAndView.addObject("genderRequest", genderRequest);
        modelAndView.setViewName(INDEX_VIEW);
        return modelAndView;
    }

    @PostMapping("/gender")
    private ModelAndView gender(@ModelAttribute GenderRequest genderRequest, HttpSession session){

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(GENDERED_RESULT_VIEW);

        GenderResult result;
        if(genderRequest.getType() == 1){
            result = service.gender(genderRequest, true);
            modelAndView.addObject("genderResult", result);
        }
        else{
            result = service.ungender(genderRequest, true);
            modelAndView.addObject("genderResult", result);
        }

        modelAndView.addObject("genderRequest", genderRequest);
        session.setAttribute("genderResult", result);

        return modelAndView;
    }

    @PostMapping("/download")
    public void downloadFile( @ModelAttribute DownloadRequest downloadRequest, HttpServletResponse resp, HttpSession session) {
        String downloadFileName= "download.txt";
        String downloadStringContent= getText(session, downloadRequest); // implement this
        try {
            OutputStream out = resp.getOutputStream();
            resp.setContentType("text/plain; charset=utf-8");
            resp.addHeader("Content-Disposition","attachment; filename=\"" + downloadFileName + "\"");
            out.write(downloadStringContent.getBytes(Charset.forName("UTF-8")));
            out.flush();
            out.close();

        } catch (IOException e) {
        }
    }

    private String getText(HttpSession session, DownloadRequest downloadRequest) {
        List<String> genderedWords = new ArrayList<>(Arrays.asList(downloadRequest.getWords()));
        GenderResult genderResult = (GenderResult) session.getAttribute("genderResult");
        String text = "";
        for (Word word : genderResult.getWords()) {
            if (isNull(word.getOptions()) || word.getOptions().isEmpty()) {
                text += word.getValue() + " ";
            }
            else
            {
                text += genderedWords.get(0) + " ";
                genderedWords.remove(0);
            }
        }
        return text;
    }


}
