package com.university.dms.controllers;

import com.university.dms.models.User;
import com.university.dms.services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RegisterController {

    private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/register"})
    public String register(Model model) {
        model.addAttribute("userTypes" , getUserTypes());
        model.addAttribute("user", new User());
        return "/authentication/register";
    }

    @PostMapping(value = "/register-user")
    public String createNewUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {

        User userExists = userService.findUserByEmail(user.getEmail());

        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }

        if (bindingResult.hasErrors()) {
            if(StringUtils.isBlank(user.getFirstName())){
                model.addAttribute("firstNameError" , true);
            }
            if(StringUtils.isBlank(user.getLastName())){
                model.addAttribute("lastNameError" , true);
            }
            if(StringUtils.isBlank(user.getEmail()) || !user.getEmail().contains("@")){
                model.addAttribute("emailError" , true);
            }
            if(StringUtils.isBlank(user.getPassword()) || user.getPassword().length() < 8){
                model.addAttribute("passwordError" , true);
            }
            model.addAttribute("userTypes" , getUserTypes());
            model.addAttribute("user", new User());
            return "/authentication/register";
        }
        else {
            userService.saveUser(user);
            return "/authentication/login";
        }

    }

    private List<String> getUserTypes(){
        List<String> userTypes = new ArrayList<>();
        userTypes.add("Student");
        userTypes.add("Supervisor");
        return userTypes;
    }
}
