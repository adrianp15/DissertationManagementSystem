package com.university.dms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping(value = {"", "/", "/index"})
    public String getIndexPage() {
        return "index";
    }

    @GetMapping(value = {"/login"})
    public String login() {
        return "/authentication/login";
    }


}
