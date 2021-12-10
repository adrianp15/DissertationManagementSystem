package com.university.dms.controller.supervisor;

import com.university.dms.model.AccountType;
import com.university.dms.model.project.*;
import com.university.dms.model.project.dissertationchapters.*;
import com.university.dms.model.project.enums.ChapterStatus;
import com.university.dms.model.project.enums.ChapterTaskFeedback;
import com.university.dms.model.project.enums.ProjectStatus;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.UploadedFileWrapper;
import com.university.dms.service.project.ProjectService;
import com.university.dms.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
            return "redirect:/projects/" + project.getId() + "/proposal-page";
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

        if (introduction.getAbstractSubtask() == null || introduction.getIntroductionSubtask() == null ||
                introduction.getScopeSubtask() == null || introduction.getApproachSubtask() == null || introduction.getOverviewSubtask() == null) {
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

            if (introduction.getAbstractSubtask() == ChapterTaskFeedback.GOOD && introduction.getIntroductionSubtask() == ChapterTaskFeedback.GOOD &&
                    introduction.getScopeSubtask() == ChapterTaskFeedback.GOOD && introduction.getApproachSubtask() == ChapterTaskFeedback.GOOD &&
                    introduction.getOverviewSubtask() == ChapterTaskFeedback.GOOD) {
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

        if (literatureReview.getIntroductionSubtask() == null || literatureReview.getOverviewSubtask() == null ||
                literatureReview.getConcernsSubtask() == null || literatureReview.getRequirementsSubtask() == null ||
                literatureReview.getLitReviewSubtask() == null || literatureReview.getLsepSubtask() == null ||
                literatureReview.getSummarySubtask() == null) {

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

            if (literatureReview.getIntroductionSubtask() == ChapterTaskFeedback.GOOD && literatureReview.getOverviewSubtask() == ChapterTaskFeedback.GOOD &&
                    literatureReview.getConcernsSubtask() == ChapterTaskFeedback.GOOD && literatureReview.getRequirementsSubtask() == ChapterTaskFeedback.GOOD &&
                    literatureReview.getLitReviewSubtask() == ChapterTaskFeedback.GOOD && literatureReview.getLsepSubtask() == ChapterTaskFeedback.GOOD &&
                    literatureReview.getSummarySubtask() == ChapterTaskFeedback.GOOD) {
                project.getDissertation().getLiteratureReview().setChapterStatus(ChapterStatus.DONE);
            } else {
                project.getDissertation().getLiteratureReview().setChapterStatus(ChapterStatus.NEEDS_REVISION_FROM_STUDENT);
            }
            projectService.saveDissertation(dissertation);
            projectService.saveProject(project);

            return "redirect:/projects/" + project.getId() + "/chapter2";
        }
    }

    @PostMapping("/post-chapter3-feedback")
    public String postFeedbackOnChapter3(@Valid Methodology methodology, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(methodology.getProjectId()));

        if (methodology.getIntroductionSubtask() == null || methodology.getMethodologySubtask() == null ||
                methodology.getPlanningSubtask() == null || methodology.getSummarySubtask() == null ||
                methodology.getJustificationSubtask() == null) {

            boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

            UploadedFileWrapper uploadedFileWrapper = new UploadedFileWrapper();

            model.addAttribute("user", user);
            model.addAttribute("isUserStudent", isUserStudent);
            model.addAttribute("project", project);
            model.addAttribute("methodology", methodology);
            model.addAttribute("uploadedFileWrapper", uploadedFileWrapper);
            model.addAttribute("missingFeedback", true);

            return "project/phases/chapter3";
        } else {
            Dissertation dissertation = project.getDissertation();
            methodology.setId(project.getDissertation().getMethodology().getId());
            methodology.setSubmittedDocument(project.getDissertation().getMethodology().getSubmittedDocument());
            dissertation.setMethodology(methodology);

            if (methodology.getIntroductionSubtask() == ChapterTaskFeedback.GOOD && methodology.getMethodologySubtask() == ChapterTaskFeedback.GOOD &&
                    methodology.getPlanningSubtask() == ChapterTaskFeedback.GOOD && methodology.getSummarySubtask() == ChapterTaskFeedback.GOOD &&
                    methodology.getJustificationSubtask() == ChapterTaskFeedback.GOOD) {
                project.getDissertation().getMethodology().setChapterStatus(ChapterStatus.DONE);
            } else {
                project.getDissertation().getMethodology().setChapterStatus(ChapterStatus.NEEDS_REVISION_FROM_STUDENT);
            }
            projectService.saveDissertation(dissertation);
            projectService.saveProject(project);

            return "redirect:/projects/" + project.getId() + "/chapter3";
        }
    }

    @PostMapping("/post-chapter4-feedback")
    public String postFeedbackOnChapter4(@Valid DevelopmentTesting developmentTesting, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(developmentTesting.getProjectId()));

        if (developmentTesting.getIntroductionSubtask() == null || developmentTesting.getSpecificationsSubtask() == null ||
                developmentTesting.getAnalysisSubtask() == null || developmentTesting.getDesignSubtask() == null ||
                developmentTesting.getDevelopmentSubtask() == null || developmentTesting.getTestingSubtask() == null ||
                developmentTesting.getChallengesSubtask() == null || developmentTesting.getCompareSubtask() == null ||
                developmentTesting.getProblemsSubtask() == null || developmentTesting.getReflectSubtask() == null ||
                developmentTesting.getDiscussionSubtask() == null || developmentTesting.getSummarySubtask() == null) {

            boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

            UploadedFileWrapper uploadedFileWrapper = new UploadedFileWrapper();

            model.addAttribute("user", user);
            model.addAttribute("isUserStudent", isUserStudent);
            model.addAttribute("project", project);
            model.addAttribute("developmentTesting", developmentTesting);
            model.addAttribute("uploadedFileWrapper", uploadedFileWrapper);
            model.addAttribute("missingFeedback", true);

            return "project/phases/chapter4";
        } else {
            Dissertation dissertation = project.getDissertation();
            developmentTesting.setId(project.getDissertation().getDevelopmentTesting().getId());
            developmentTesting.setSubmittedDocument(project.getDissertation().getDevelopmentTesting().getSubmittedDocument());
            dissertation.setDevelopmentTesting(developmentTesting);

            if (developmentTesting.getIntroductionSubtask() == ChapterTaskFeedback.GOOD && developmentTesting.getSpecificationsSubtask() == ChapterTaskFeedback.GOOD &&
                    developmentTesting.getAnalysisSubtask() == ChapterTaskFeedback.GOOD && developmentTesting.getDesignSubtask() == ChapterTaskFeedback.GOOD &&
                    developmentTesting.getDevelopmentSubtask() == ChapterTaskFeedback.GOOD && developmentTesting.getTestingSubtask() == ChapterTaskFeedback.GOOD &&
                    developmentTesting.getChallengesSubtask() == ChapterTaskFeedback.GOOD && developmentTesting.getCompareSubtask() == ChapterTaskFeedback.GOOD &&
                    developmentTesting.getProblemsSubtask() == ChapterTaskFeedback.GOOD && developmentTesting.getReflectSubtask() == ChapterTaskFeedback.GOOD &&
                    developmentTesting.getDiscussionSubtask() == ChapterTaskFeedback.GOOD && developmentTesting.getSummarySubtask() == ChapterTaskFeedback.GOOD) {
                project.getDissertation().getDevelopmentTesting().setChapterStatus(ChapterStatus.DONE);
            } else {
                project.getDissertation().getDevelopmentTesting().setChapterStatus(ChapterStatus.NEEDS_REVISION_FROM_STUDENT);
            }
            projectService.saveDissertation(dissertation);
            projectService.saveProject(project);

            return "redirect:/projects/" + project.getId() + "/chapter4";
        }
    }

    @PostMapping("/post-chapter5-feedback")
    public String postFeedbackOnChapter5(@Valid Conclusion conclusion, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(conclusion.getProjectId()));

        if (conclusion.getSummarySubtask() == null || conclusion.getConclusionsSubtask() == null ||
                conclusion.getRecommendationsSubtask() == null || conclusion.getFutureWorkSubtask() == null) {

            boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

            UploadedFileWrapper uploadedFileWrapper = new UploadedFileWrapper();

            model.addAttribute("user", user);
            model.addAttribute("isUserStudent", isUserStudent);
            model.addAttribute("project", project);
            model.addAttribute("conclusion", conclusion);
            model.addAttribute("uploadedFileWrapper", uploadedFileWrapper);
            model.addAttribute("missingFeedback", true);

            return "project/phases/chapter5";
        } else {
            Dissertation dissertation = project.getDissertation();
            conclusion.setId(project.getDissertation().getConclusion().getId());
            conclusion.setSubmittedDocument(project.getDissertation().getConclusion().getSubmittedDocument());
            dissertation.setConclusion(conclusion);

            if (conclusion.getSummarySubtask() == ChapterTaskFeedback.GOOD && conclusion.getConclusionsSubtask() == ChapterTaskFeedback.GOOD &&
                    conclusion.getRecommendationsSubtask() == ChapterTaskFeedback.GOOD && conclusion.getFutureWorkSubtask() == ChapterTaskFeedback.GOOD) {
                project.getDissertation().getConclusion().setChapterStatus(ChapterStatus.DONE);
            } else {
                project.getDissertation().getConclusion().setChapterStatus(ChapterStatus.NEEDS_REVISION_FROM_STUDENT);
            }
            projectService.saveDissertation(dissertation);
            projectService.saveProject(project);

            return "redirect:/projects/" + project.getId() + "/chapter5";
        }
    }

    @PostMapping("/post-chapter6-feedback")
    public String postFeedbackOnChapter6(@Valid PresentationReferences presentationReferences, BindingResult bindingResult, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(presentationReferences.getProjectId()));

        if (presentationReferences.getReportSubtask() == null || presentationReferences.getFrontSectionSubtask() == null ||
                presentationReferences.getDiscussionSubtask() == null || presentationReferences.getLanguageSubtask() == null ||
                presentationReferences.getReferencingSubtask() == null || presentationReferences.getAppendixSubtask() == null ||
                presentationReferences.getOtherDocsSubtask() == null) {

            boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

            UploadedFileWrapper uploadedFileWrapper = new UploadedFileWrapper();

            model.addAttribute("user", user);
            model.addAttribute("isUserStudent", isUserStudent);
            model.addAttribute("project", project);
            model.addAttribute("presentationReferences", presentationReferences);
            model.addAttribute("uploadedFileWrapper", uploadedFileWrapper);
            model.addAttribute("missingFeedback", true);

            return "project/phases/chapter6";
        } else {
            Dissertation dissertation = project.getDissertation();
            presentationReferences.setId(project.getDissertation().getPresentationReferences().getId());
            presentationReferences.setSubmittedDocument(project.getDissertation().getPresentationReferences().getSubmittedDocument());
            dissertation.setPresentationReferences(presentationReferences);

            if (presentationReferences.getReportSubtask() == ChapterTaskFeedback.GOOD && presentationReferences.getReportSubtask() == ChapterTaskFeedback.GOOD &&
                    presentationReferences.getDiscussionSubtask() == ChapterTaskFeedback.GOOD && presentationReferences.getLanguageSubtask() == ChapterTaskFeedback.GOOD &&
                    presentationReferences.getReferencingSubtask() == ChapterTaskFeedback.GOOD && presentationReferences.getAppendixSubtask() == ChapterTaskFeedback.GOOD &&
                    presentationReferences.getOtherDocsSubtask() == ChapterTaskFeedback.GOOD) {
                project.getDissertation().getPresentationReferences().setChapterStatus(ChapterStatus.DONE);
            } else {
                project.getDissertation().getPresentationReferences().setChapterStatus(ChapterStatus.NEEDS_REVISION_FROM_STUDENT);
            }
            projectService.saveDissertation(dissertation);
            projectService.saveProject(project);

            return "redirect:/projects/" + project.getId() + "/chapter6";
        }
    }

    @GetMapping(value = "/{id}/supervisory-records")
    public String viewSupervisoryRecordsPage(Model model,  @PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(id));

       List<SupervisoryRecord> supervisoryRecords = projectService.findAllBySupervisor(user);

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("supervisoryRecords", supervisoryRecords);
        return "supervisor/supervisoryrecords";
    }

    @GetMapping(value = "/{id}/new-supervisory-record")
    public String newSupervisoryRecord(Model model,  @PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(id));

        SupervisoryRecord supervisoryRecord = new SupervisoryRecord();

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("supervisoryRecord", supervisoryRecord);
        return "supervisor/newsupervisoryrecord";
    }

    @GetMapping(value = "/supervisory-record/{projectid}/{recordid}")
    public String viewSupervisoryRecord(Model model,  @PathVariable("projectid") String projectid, @PathVariable("recordid") String recordid){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(projectid));

        SupervisoryRecord supervisoryRecord = projectService.findSupervisoryRecordById(Integer.parseInt(recordid));

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("supervisoryRecord", supervisoryRecord);
        return "supervisor/viewrecord";
    }

    @PostMapping(value = "/submit-supervisory-record")
    public String submitNewMeeting(@Valid @ModelAttribute("supervisoryRecord") SupervisoryRecord supervisoryRecord, BindingResult result, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(supervisoryRecord.getProjectId()));

        if(supervisoryRecord.getDate() == null || StringUtils.isBlank(supervisoryRecord.getContactFormat()) || StringUtils.isBlank(supervisoryRecord.getStudentAttendance()) ||
                StringUtils.isBlank(supervisoryRecord.getReasonStudentNotAttending()) || StringUtils.isBlank(supervisoryRecord.getStudentCompleteAgreedPoints()) ||
                StringUtils.isBlank(supervisoryRecord.getPointsCoveredInMeeting()) || StringUtils.isBlank(supervisoryRecord.getAgreedPointsFromMeeting()) ||
                StringUtils.isBlank(supervisoryRecord.getStudentCommitment()) || StringUtils.isBlank(supervisoryRecord.getConcerns())){
            model.addAttribute("user", user);
            model.addAttribute("project", project);
            model.addAttribute("supervisoryRecord", supervisoryRecord);
            return "supervisor/newsupervisoryrecord";
        } else {
            supervisoryRecord.setStudent(project.getStudent());
            supervisoryRecord.setSupervisor(project.getSupervisor());
            projectService.saveSupervisoryRecord(supervisoryRecord);
        }

        return "redirect:/" + project.getId() + "/supervisory-records";
    }

}
