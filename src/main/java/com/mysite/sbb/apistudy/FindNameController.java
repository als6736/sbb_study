package com.mysite.sbb.apistudy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FindNameController {
    @Autowired
    private SubstationInfoRepository infoRepository;

    @GetMapping("findname")
    public String input_name(Model model){
        return "findname";
    }


}
