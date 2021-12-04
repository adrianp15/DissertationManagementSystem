package com.university.dms.controller.supervisor;

import com.university.dms.model.project.*;
import com.university.dms.model.project.enums.ProjectStatus;
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
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SupervisorController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;


    @PostMapping("/post-proposal-marking")
    public String postFeedbackOnSuggestion(@Valid ProposalMarking proposalMarking, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        //Project project1 = projectService.findProjectById(Integer.parseInt(id));

        Project project = projectService.findProjectById(proposalMarking.getId());

        Proposal proposal = project.getProposal();

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("project", project);
            model.addAttribute("proposal", project.getProposal());
            model.addAttribute("proposalMarking", proposalMarking);
            return "project/proposalpage";
        } else {
            proposal.setProposalMarking(proposalMarking);
            int finalGrade = proposalMarking.getIntroductionMark() + proposalMarking.getAimsObjectivesMark() +
                                 proposalMarking.getMethodologyMark() + proposalMarking.getProjectPlanMark() +
                                 proposalMarking.getSummaryConclusionMark() + proposalMarking.getPresentationAppendicesMark();

            project.setProjectStatus(finalGrade > 39 ? ProjectStatus.PROPOSAL_APPROVED : ProjectStatus.PROPOSAL_REJECTED);

            proposal.setGrade(Integer.toString(finalGrade));
            projectService.saveProposal(proposal);
            return "redirect:/projects/" + project.getId() +"/proposal-page";
        }

        //return "redirect:/projects/" + project.getId() + "/proposal-page";
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
