package com.mysite.sbb.ela;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data")
public class ElaDataController {

    private final ElaDataService elaDataService;

    public ElaDataController(ElaDataService elaDataService) {
        this.elaDataService = elaDataService;
    }

    @GetMapping
    public void fetchData() {
        elaDataService.fetchDataAndSendToLogstash();
    }
}
