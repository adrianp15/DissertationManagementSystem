package com.university.dms.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {



    @GetMapping(value = {"/login"})
    public String login() {
        return "/authentication/login";
    }

    // TEST COMMIT
    // TEST COMMIT
    // TEST COMMIT
    // TEST COMMIT


}
