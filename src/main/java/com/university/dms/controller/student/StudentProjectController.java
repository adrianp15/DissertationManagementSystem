package com.university.dms.controller.student;

import com.university.dms.model.project.Project;
import com.university.dms.model.project.ProjectStatus;
import com.university.dms.model.project.Suggestion;
import com.university.dms.model.user.User;
import com.university.dms.service.project.ProjectService;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
public class StudentProjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/student-my-projects")
    public String myProjects(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        List<Project> studentProjects = projectService.getCurrentStudentProjects(user);

        model.addAttribute("user", user);
        model.addAttribute("studentProjects", studentProjects);

        return "student/myprojects";
    }

    @GetMapping("/addproject")
    public String addNewProject(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Suggestion suggestion = new Suggestion();


        model.addAttribute("user", user);
        model.addAttribute("suggestion", suggestion);

        return "project/addproject";
    }

    @RequestMapping(value = "submit-proposal")
    public String submitSuggestion(@Valid Suggestion suggestion, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        if(suggestion.getTitle().isBlank() || suggestion.getDescription().isBlank() || suggestion.getMethodology().isBlank() ||
                suggestion.getDescription().isBlank() || suggestion.getLsep().isBlank()) {

            suggestion = new Suggestion();

            model.addAttribute("user", user);
            model.addAttribute("suggestion", suggestion);

            return "project/addproject";

        } else {
            Project project = new Project();
            project.setSuggestion(suggestion);
            project.setStartDate(LocalDate.now());
            project.setStudent(user);
            project.setProjectStatus(ProjectStatus.SUGGESTION_SUBMITTED);

            projectService.saveProject(project);
        }

        return "redirect:/student-my-projects";
    }

}
