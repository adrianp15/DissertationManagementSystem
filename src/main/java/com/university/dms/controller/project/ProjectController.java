package com.university.dms.controller.project;

import com.university.dms.Utils.ImageUtility;
import com.university.dms.model.Image;
import com.university.dms.model.project.Project;
import com.university.dms.model.project.ProjectStatus;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.ProposalWrapper;
import com.university.dms.service.project.ProjectService;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.*;
import com.cloudmersive.client.ConvertDocumentApi;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
