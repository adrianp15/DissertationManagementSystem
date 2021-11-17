package com.university.dms.controller.coordinator;

import com.university.dms.model.user.User;
import com.university.dms.model.utils.StringWrapper;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CoordinatorController {

    @Autowired
    private UserService userService;



    @GetMapping("/users")
    public String userProfile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        List<User> users = userService.findAll();
        users = users.stream().limit(10).collect(Collectors.toList());

        StringWrapper search_data = new StringWrapper();

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "coordinator/users";
    }

    @PostMapping("/searchuser")
    public String searchUsers(@RequestParam String search_data, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        List<User> users = userService.coordinatorSearchUser(search_data);

        users = users.stream().limit(10).collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("users", users);
        return "coordinator/users";
    }



    @GetMapping("/users/{userId}")
    public String viewUserProfile(Model model, @PathVariable("userId") String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());

        User user = userService.findUserById(Integer.parseInt(userId));

        Map<Boolean, String> accountEnabledOptions = new HashMap<>();
        accountEnabledOptions.put(true, "Yes");
        accountEnabledOptions.put(false, "No");

        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("accountEnabledOptions", accountEnabledOptions);

        return "coordinator/coordinatorviewuserprofile";
    }

    @PostMapping(value = "/coordinatorupdateuser")
    public String coordinatorUpdateUser(@Valid User updatedUser, BindingResult bindingResult) {

        User user = userService.findUserByEmail(updatedUser.getEmail());

        user.setActive(updatedUser.getActive());
        user.setAccountType(updatedUser.getAccountType());

        userService.updateUser(user);
        return "redirect:/users/" + user.getId();
    }
}
