package com.mysite.sbb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class root {

    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
