package com.university.dms.controller.supervisor;

import com.university.dms.model.project.Project;
import com.university.dms.model.project.Proposal;
import com.university.dms.model.project.Suggestion;
import com.university.dms.model.user.User;
import com.university.dms.service.project.ProjectService;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class SupervisorController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;


    @PostMapping("/post-proposal-feedback")
    public String postFeedbackOnSuggestion(@Valid Proposal proposal, Model model) {

        Project project = projectService.findProjectById(proposal.getId());

        Proposal proposal1 = project.getProposal();

        proposal1.setFeedback(proposal.getFeedback());

        projectService.saveProposal(proposal1);

        return "redirect:/projects/" + project.getId() + "/proposal-page";
    }

    @GetMapping("/supervisor-my-projects")
    public String viewMyProjects(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        List<Project> supervisorProjects = projectService.getSupervisorProjects(user);

        model.addAttribute("user", user);
        model.addAttribute("supervisorProjects", supervisorProjects);

        return "supervisor/supervisorviewprojects";
    }

}
