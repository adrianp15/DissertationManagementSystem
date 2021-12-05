package com.university.dms.controller.supervisor;

import com.university.dms.model.AccountType;
import com.university.dms.model.project.*;
import com.university.dms.model.project.dissertationchapters.Introduction;
import com.university.dms.model.project.dissertationchapters.LiteratureReview;
import com.university.dms.model.project.enums.ChapterStatus;
import com.university.dms.model.project.enums.ChapterTaskFeedback;
import com.university.dms.model.project.enums.ProjectStatus;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.UploadedFileWrapper;
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
    public String postFeedbackOnProposal(@Valid ProposalMarking proposalMarking, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        //Project project1 = projectService.findProjectById(Integer.parseInt(id));

        Project project = projectService.findProjectById(Integer.parseInt(proposalMarking.getProjectId()));

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

    @PostMapping("/post-chapter1-feedback")
    public String postFeedbackOnChapter1(@Valid Introduction introduction, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(introduction.getProjectId()));

        if(introduction.getAbstractSubtask() == null || introduction.getIntroductionSubtask() == null ||
           introduction.getScopeSubtask() == null || introduction.getApproachSubtask() == null || introduction.getOverviewSubtask() == null){
            boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

            UploadedFileWrapper uploadedFileWrapper = new UploadedFileWrapper();

            model.addAttribute("user", user);
            model.addAttribute("isUserStudent", isUserStudent);
            model.addAttribute("project", project);
            model.addAttribute("introduction", introduction);
            model.addAttribute("uploadedFileWrapper", uploadedFileWrapper);
            model.addAttribute("missingFeedback", true);

            return "project/phases/chapter1";
        } else {
            Dissertation dissertation = project.getDissertation();
            introduction.setId(project.getDissertation().getIntroduction().getId());
            introduction.setSubmittedDocument(project.getDissertation().getIntroduction().getSubmittedDocument());
            dissertation.setIntroduction(introduction);

            if(introduction.getAbstractSubtask() == ChapterTaskFeedback.GOOD && introduction.getIntroductionSubtask() == ChapterTaskFeedback.GOOD &&
                    introduction.getScopeSubtask() == ChapterTaskFeedback.GOOD && introduction.getApproachSubtask() == ChapterTaskFeedback.GOOD &&
                    introduction.getOverviewSubtask() == ChapterTaskFeedback.GOOD){
                project.getDissertation().getIntroduction().setChapterStatus(ChapterStatus.DONE);
            } else {
                project.getDissertation().getIntroduction().setChapterStatus(ChapterStatus.NEEDS_REVISION_FROM_STUDENT);
            }
            projectService.saveDissertation(dissertation);
            projectService.saveProject(project);

            return "redirect:/projects/" + project.getId() + "/chapter1";
        }
    }

    @PostMapping("/post-chapter2-feedback")
    public String postFeedbackOnChapter2(@Valid LiteratureReview literatureReview, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(literatureReview.getProjectId()));

        if(literatureReview.getIntroductionSubtask() == null || literatureReview.getOverviewSubtask() == null ||
                literatureReview.getConcernsSubtask() == null || literatureReview.getRequirementsSubtask() == null ||
                literatureReview.getLitReviewSubtask() == null || literatureReview.getLsepSubtask() == null ||
                literatureReview.getSummarySubtask() == null){

            boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

            UploadedFileWrapper uploadedFileWrapper = new UploadedFileWrapper();

            model.addAttribute("user", user);
            model.addAttribute("isUserStudent", isUserStudent);
            model.addAttribute("project", project);
            model.addAttribute("literatureReview", literatureReview);
            model.addAttribute("uploadedFileWrapper", uploadedFileWrapper);
            model.addAttribute("missingFeedback", true);

            return "project/phases/chapter2";
        } else {
            Dissertation dissertation = project.getDissertation();
            literatureReview.setId(project.getDissertation().getLiteratureReview().getId());
            literatureReview.setSubmittedDocument(project.getDissertation().getLiteratureReview().getSubmittedDocument());
            dissertation.setLiteratureReview(literatureReview);

            if(literatureReview.getIntroductionSubtask() == ChapterTaskFeedback.GOOD && literatureReview.getOverviewSubtask() == ChapterTaskFeedback.GOOD &&
                    literatureReview.getConcernsSubtask() == ChapterTaskFeedback.GOOD && literatureReview.getRequirementsSubtask() == ChapterTaskFeedback.GOOD &&
                    literatureReview.getLitReviewSubtask() == ChapterTaskFeedback.GOOD && literatureReview.getLsepSubtask() == ChapterTaskFeedback.GOOD &&
                    literatureReview.getSummarySubtask() == ChapterTaskFeedback.GOOD){
                project.getDissertation().getLiteratureReview().setChapterStatus(ChapterStatus.DONE);
            } else {
                project.getDissertation().getLiteratureReview().setChapterStatus(ChapterStatus.NEEDS_REVISION_FROM_STUDENT);
            }
            projectService.saveDissertation(dissertation);
            projectService.saveProject(project);

            return "redirect:/projects/" + project.getId() + "/chapter2";
        }
    }

}
