package com.university.dms.controller.project;

import com.university.dms.model.project.Project;
import com.university.dms.model.project.enums.ProjectStatus;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.ProposalWrapper;
import com.university.dms.service.project.ProjectService;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;




    @GetMapping("/suggestions/{id}")
    public String viewSuggestion(Model model, @PathVariable("id") String projectId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(projectId));
        List<User> supervisors = userService.getSupervisors();

        model.addAttribute("user", currentUser);
        model.addAttribute("project", project);
        model.addAttribute("supervisors", supervisors);
        model.addAttribute("suggestion", project.getSuggestion());

        return "project/viewsuggestion";
    }

    @GetMapping(value = "/projects/{id}/{action}")
    public String approveRejectProposal(Model model, @PathVariable("id") String id, @PathVariable("action") String action) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Project project = projectService.findProjectById(Integer.parseInt(id));

        switch (action){
            case "approve":
                project.setProjectStatus(ProjectStatus.PROPOSAL_APPROVED);
                break;
            case "reject":
                project.setProjectStatus(ProjectStatus.PROPOSAL_REJECTED);
                break;
        }
        projectService.saveProject(project);


        ProposalWrapper proposalWrapper = new ProposalWrapper();
        proposalWrapper.setProjectId(Integer.parseInt(id));

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("proposal", project.getProposal());
        model.addAttribute("proposalWrapper", proposalWrapper);

        return "project/proposalpage";
    }




}
