package com.university.dms.controllers;

import com.university.dms.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterController {


    @GetMapping(value = {"/register"})
    public String register(Model model) {
        model.addAttribute("userTypes" , getUserTypes());
        model.addAttribute("user", new User());
        return "/authentication/register";
    }

    private List<String> getUserTypes(){
        List<String> userTypes = new ArrayList<>();
        userTypes.add("Student");
        userTypes.add("Supervisor");
        return userTypes;
    }


}
