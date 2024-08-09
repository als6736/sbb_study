package com.mysite.sbb.ela;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SubwayStatsController {

    @Autowired
    private SubwayStatsService subwayStatsService;

    @GetMapping("/statsList")
    public String getSubwayStats(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "1000") int size) {
        List<SubwayStats> statsList = subwayStatsService.getAllStats(page, size);
        model.addAttribute("stats", statsList);
        return "statsList";
    }
}
