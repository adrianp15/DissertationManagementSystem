package com.university.dms.controller.coordinator;

import com.university.dms.model.project.Project;
import com.university.dms.model.project.ProjectStatus;
import com.university.dms.model.project.Suggestion;
import com.university.dms.model.user.User;
import com.university.dms.service.project.ProjectService;
import com.university.dms.service.project.SuggestionService;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class CoordinatorController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SuggestionService suggestionService;


    @GetMapping("/users")
    public String userProfile(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        List<User> users = userService.findAll();
        users = users.stream().limit(10).collect(Collectors.toList());


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

    @GetMapping(value = "/coordinatorviewprojects")
    public String viewAllProjects(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());
        List<User> allUsers = userService.findAll();
        List<Project> allProjects = projectService.findAll();

        ArrayList<String> filter = new ArrayList<>();
        filter.add("Pending approval");
        filter.add("Have supervisors assigned");

        model.addAttribute("user", currentUser);
        model.addAttribute("projectFilters", filter);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("allProjects", allProjects);

        return "coordinator/coordinatorviewallprojects";
    }


    @GetMapping (value = "/assignsupervisor/{projectid}/{supervisorid}")
    public String coordinatorAssignSupervisor(@PathVariable("projectid") String projectid,
                                              @PathVariable("supervisorid") String supervisorid, Model model){

        Project project = projectService.findProjectById(Integer.parseInt(projectid));
        User supervisor1 = userService.findUserById(Integer.parseInt(supervisorid));

        project.setSupervisor(supervisor1);
        project.setProjectStatus(ProjectStatus.SUGGESTION_APPROVED);

        projectService.setSupervisor(project);
        return "redirect:/coordinatorviewprojects/";

    }

    @PostMapping("/post-suggestion-feedback")
    public String postFeedbackOnSuggestion(@Valid Suggestion suggestion, Model model) {

        Suggestion suggestion1 = suggestionService.findSuggestionById(suggestion.getId());

        suggestion1.setFeedback(suggestion.getFeedback());

        suggestionService.saveSuggestion(suggestion1);

        return "redirect:/coordinatorviewprojects/";
    }

    @PostMapping("/searchforproject")
    public String searchProject(@RequestParam String search_data, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());
        List<Project> allProjects = projectService.searchProject(search_data);

        Set<User> allUsers = new HashSet<>();

        for (Project project:allProjects){
            allUsers.add(project.getStudent());
        }

        HashMap<Integer, String> filter = new HashMap<>();
        filter.put(0, "Pending approval");
        filter.put(1, "Have supervisors assigned");


        model.addAttribute("user", currentUser);
        model.addAttribute("projectFilters", filter);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("allProjects", allProjects);
        return "coordinator/coordinatorviewallprojects";
    }

}
