package com.university.dms.controller.user;

import com.university.dms.model.User;
import com.university.dms.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public String userProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        model.addAttribute("user", user);
        return "mainpage/userprofile";
    }

    @PostMapping(value = "/update-user")
    public String updateUser(@Valid User updatedUser, BindingResult bindingResult) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        if(!StringUtils.isBlank(updatedUser.getPassword())) {
            user.setPassword(updatedUser.getPassword());
        }

        if(!StringUtils.isBlank(updatedUser.getEmail())) {
            user.setEmail(updatedUser.getEmail());
        }

        if(!StringUtils.isBlank(updatedUser.getSkype())) {
            user.setSkype(updatedUser.getSkype());
        }

        if(!StringUtils.isBlank(updatedUser.getPhone())) {
            user.setPhone(updatedUser.getPhone());
        }

        if(!StringUtils.isBlank(updatedUser.getOther_contact())) {
            user.setOther_contact(updatedUser.getOther_contact());
        }

        userService.updateUser(user);
        return "redirect:/profile";
    }
}
