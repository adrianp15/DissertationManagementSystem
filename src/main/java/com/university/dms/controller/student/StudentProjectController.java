package com.university.dms.controller.student;

import com.cloudmersive.client.ConvertDocumentApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.university.dms.model.project.*;
import com.university.dms.model.project.dissertationchapters.*;
import com.university.dms.model.project.enums.ChapterStatus;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.*;

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
        studentProjects.sort((a, b) -> Boolean.compare(a.getPreferredOption(), b.getPreferredOption()));
        Collections.reverse(studentProjects);

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

        if (suggestion.getTitle().isBlank() || suggestion.getDescription().isBlank() || suggestion.getMethodology().isBlank() ||
                suggestion.getDescription().isBlank() || suggestion.getLsep().isBlank()) {

            suggestion = new Suggestion();

            model.addAttribute("user", user);
            model.addAttribute("suggestion", suggestion);

            return "project/addproject";

        } else {
            Project project = new Project();
            project.setTitle(suggestion.getTitle());
            project.setSuggestion(suggestion);
            project.setStartDate(LocalDate.now());
            project.setStudent(user);
            project.setProjectStatus(ProjectStatus.SUGGESTION_SUBMITTED);
            project.setPreferredOption(false);
            project.setProjectType(suggestion.getProjectType());

            projectService.saveProject(project);
        }

        return "redirect:/student-my-projects";
    }

    @RequestMapping(value = "/set-as-preffered-option/{id}")
    public String setPreferredProject(Model model, @PathVariable("id") String id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());

        projectService.setPreferredProject(currentUser, Integer.parseInt(id));


        return "redirect:/student-my-projects";
    }

    @GetMapping(value = "/projects/{id}")
    public String viewProject(Model model, @PathVariable("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Project project = projectService.findProjectById(Integer.parseInt(id));

        List<ProjectStatus> allStatusesList = Arrays.asList(ProjectStatus.class.getEnumConstants());
        List<ProjectStatus> currentProjectStatuses = new ArrayList<>();

        for(ProjectStatus projectStatus:allStatusesList){
            if(projectStatus==ProjectStatus.PROPOSAL_REJECTED){
                if(project.getProjectStatus()==ProjectStatus.PROPOSAL_REJECTED){
                    currentProjectStatuses.add(projectStatus);
                    break;
                }
            }

            if(projectStatus==project.getProjectStatus()){
                currentProjectStatuses.add(projectStatus);
                break;
            }
            currentProjectStatuses.add(projectStatus);
        }

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("currentProjectStatuses", currentProjectStatuses);

        return "project/projectpage";
    }

    @PostMapping("/change-project-title")
    public String changeProjectTitle(@Valid Project project, Model model) {

        Project project1 = projectService.findProjectById(project.getId());

        project1.setTitle(project.getTitle());

        projectService.saveProject(project1);

        return "redirect:/projects/" + project1.getId();
    }

    @PostMapping("/upload/{type}")
    public String uploadChapter1(@PathVariable("type") String type, @Valid UploadedFileWrapper uploadedFileWrapper)
            throws IOException, URISyntaxException {

        Project project = projectService.findProjectById(uploadedFileWrapper.getProjectId());

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setConnectTimeout(30000);
        defaultClient.setReadTimeout(30000);
        ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        Apikey.setApiKey("815982a1-05a5-426b-9638-ba085d169705");

        MultipartFile file = uploadedFileWrapper.getDocument();

        if (!file.isEmpty()) {
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getName());
            file.transferTo(convFile);

            byte[] result = null;

            if (file.getOriginalFilename().contains("pdf")) {

                result = Files.readAllBytes(convFile.toPath());

            } else {

                ConvertDocumentApi apiInstance = new ConvertDocumentApi();
                try {

                    result = apiInstance.convertDocumentDocxToPdf(convFile);

                } catch (ApiException e) {
                    System.err.println("Exception when calling ConvertDocumentApi#convertDocumentDocxToPdf");
                    e.printStackTrace();
                }
            }

            switch (type){
                case "proposal":
                    Proposal proposal = new Proposal();
                    proposal.setDocument(result);
//            projectService.saveProposal(proposal);

                    project.setProposal(proposal);
                    project.setProjectStatus(ProjectStatus.PROPOSAL_SUBMITTED);

                    Dissertation dissertation = new Dissertation();

                    dissertation.setIntroduction(new Introduction());
                    dissertation.getIntroduction().setChapterStatus(ChapterStatus.NOT_STARTED);

                    dissertation.setLiteratureReview(new LiteratureReview());
                    dissertation.getLiteratureReview().setChapterStatus(ChapterStatus.NOT_STARTED);

                    dissertation.setMethodology(new Methodology());
                    dissertation.getMethodology().setChapterStatus(ChapterStatus.NOT_STARTED);

                    dissertation.setDevelopmentTesting(new DevelopmentTesting());
                    dissertation.getDevelopmentTesting().setChapterStatus(ChapterStatus.NOT_STARTED);

                    dissertation.setConclusion(new Conclusion());
                    dissertation.getConclusion().setChapterStatus(ChapterStatus.NOT_STARTED);

                    dissertation.setPresentationReferences(new PresentationReferences());
                    dissertation.getPresentationReferences().setChapterStatus(ChapterStatus.NOT_STARTED);

                    project.setDissertation(dissertation);
                    break;
                case "chapter1":
                    project.getDissertation().getIntroduction().setChapterStatus(ChapterStatus.NEW_WORK_SUBMITTED);
                    project.getDissertation().getIntroduction().setSubmittedDocument(result);
                    break;

                case "chapter2":
                    project.getDissertation().getLiteratureReview().setChapterStatus(ChapterStatus.NEW_WORK_SUBMITTED);
                    project.getDissertation().getLiteratureReview().setSubmittedDocument(result);
                    break;
            }

            projectService.saveProject(project);
        }

        return "redirect:/projects/" + project.getId();
    }


}
