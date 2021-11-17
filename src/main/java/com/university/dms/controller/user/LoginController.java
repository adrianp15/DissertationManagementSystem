package com.university.dms.controller.user;

import com.university.dms.model.user.User;
import com.university.dms.repository.user.RoleRepository;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value={ "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/authentication/login");
        return modelAndView;
    }

    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();

//        ArrayList<String> accTypes = new ArrayList<>();
//        accTypes.add("Student");
//        accTypes.add("Supervisor");
//
//        modelAndView.addObject("accTypes", accTypes);

        modelAndView.addObject("user", user);
        modelAndView.setViewName("/authentication/register");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public String createNewUser(@Valid User user, BindingResult bindingResult) {
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
           return "/authentication/register";
        } else {
            userService.saveUser(user);
            return "redirect:/login";
        }
    }

}
