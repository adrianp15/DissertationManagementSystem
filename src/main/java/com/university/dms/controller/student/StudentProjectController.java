package com.university.dms.controller.student;

import com.cloudmersive.client.ConvertDocumentApi;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.university.dms.model.AccountType;
import com.university.dms.model.project.*;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.ProposalWrapper;
import com.university.dms.service.project.ProjectService;
import com.university.dms.service.user.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
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

    @GetMapping(value = "/projects/{id}/proposal-page")
    public String viewProposalPage(Model model, @PathVariable("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Project project = projectService.findProjectById(Integer.parseInt(id));

        boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

        ProposalMarking proposalMarking = project.getProposal() == null ? new ProposalMarking() : project.getProposal().getProposalMarking() == null ? new ProposalMarking() : project.getProposal().getProposalMarking();

        model.addAttribute("user", user);
        model.addAttribute("isUserStudent", isUserStudent);
        model.addAttribute("project", project);
        model.addAttribute("proposal", project.getProposal());
        model.addAttribute("proposalMarking", proposalMarking);

        return "project/proposalpage";
    }

    @PostMapping("/upload/proposal")
    public String uploadProposal(@Valid ProposalWrapper proposalWrapper)
            throws IOException, URISyntaxException {

        Project project = projectService.findProjectById(proposalWrapper.getProjectId());

        ApiClient defaultClient = Configuration.getDefaultApiClient();
        defaultClient.setConnectTimeout(30000);
        defaultClient.setReadTimeout(30000);
        ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        Apikey.setApiKey("815982a1-05a5-426b-9638-ba085d169705");

        MultipartFile file = proposalWrapper.getDocument();

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

            Proposal proposal = new Proposal();
            proposal.setDocument(result);
            projectService.saveProposal(proposal);

            project.setProposal(proposal);
            project.setProjectStatus(ProjectStatus.PROPOSAL_SUBMITTED);
            projectService.saveProject(project);
        }

        return "redirect:/projects/" + project.getId() + "/proposal-page";
    }

    @GetMapping("/get/proposal/{id}")
    public ResponseEntity<byte[]> getProposal(@PathVariable("id") String id, HttpServletResponse resp) throws IOException, SQLException {

        Project project = projectService.findProjectById(Integer.parseInt(id));

        byte[] byteArray = project.getProposal().getDocument();

        Blob blob = new javax.sql.rowset.serial.SerialBlob(byteArray);

        File outputFile = new File(System.getProperty("java.io.tmpdir") + "/" + project.getId() + ".pdf");
        FileOutputStream fout = new FileOutputStream(outputFile);
        IOUtils.copy(blob.getBinaryStream(), fout);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("application/pdf"))
                .body(byteArray);
    }

}
