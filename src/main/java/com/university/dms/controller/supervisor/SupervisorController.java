package com.university.dms.controller.supervisor;

import com.university.dms.model.project.Project;
import com.university.dms.model.project.Proposal;
import com.university.dms.model.project.Suggestion;
import com.university.dms.service.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class SupervisorController {

    @Autowired
    private ProjectService projectService;



    @PostMapping("/post-proposal-feedback")
    public String postFeedbackOnSuggestion(@Valid Proposal proposal, Model model) {

        Project project = projectService.findProjectById(proposal.getId());

        Proposal proposal1 = project.getProposal();

        proposal1.setFeedback(proposal.getFeedback());

        projectService.saveProposal(proposal1);

        return "redirect:/projects/" + project.getId() + "/proposal-page";
    }
}
