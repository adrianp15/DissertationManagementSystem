package com.university.dms.controller.project;

import com.university.dms.model.AccountType;
import com.university.dms.model.project.Project;
import com.university.dms.model.project.ProposalMarking;
import com.university.dms.model.project.dissertationchapters.Introduction;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.UploadedFileWrapper;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
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

    // fetch the proposal document from the database
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

    // fetch a chapter document from the database
    @GetMapping("/get/{chapter}/{id}")
    public ResponseEntity<byte[]> getDissertationChapterDocument(@PathVariable("id") String id, @PathVariable("chapter") String chapter, HttpServletResponse resp) throws IOException, SQLException {

        Project project = projectService.findProjectById(Integer.parseInt(id));

        byte[] byteArray = new byte[0];

        switch (chapter){
            case "chapter1":
                byteArray = project.getDissertation().getIntroduction().getSubmittedDocument();
                break;
            case "chapter2":
                break;
        }


        Blob blob = new javax.sql.rowset.serial.SerialBlob(byteArray);

        File outputFile = new File(System.getProperty("java.io.tmpdir") + "/" + project.getId() + ".pdf");
        FileOutputStream fout = new FileOutputStream(outputFile);
        IOUtils.copy(blob.getBinaryStream(), fout);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf("application/pdf"))
                .body(byteArray);
    }


    @GetMapping(value = "/projects/{id}/proposal-page")
    public String viewProposalPage(Model model, @PathVariable("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Project project = projectService.findProjectById(Integer.parseInt(id));

        boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

        ProposalMarking proposalMarking = project.getProposal() == null ? new ProposalMarking() : project.getProposal().getProposalMarking() == null ? new ProposalMarking() : project.getProposal().getProposalMarking();
        UploadedFileWrapper uploadedFileWrapper = new UploadedFileWrapper();

        model.addAttribute("user", user);
        model.addAttribute("isUserStudent", isUserStudent);
        model.addAttribute("project", project);
        model.addAttribute("proposal", project.getProposal());
        model.addAttribute("proposalMarking", proposalMarking);
        model.addAttribute("proposalWrapper", uploadedFileWrapper);

        return "project/proposalpage";
    }



    @GetMapping(value = "/projects/{id}/chapter1")
    public String getDissertationChapter1(Model model, @PathVariable("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Project project = projectService.findProjectById(Integer.parseInt(id));

        boolean isUserStudent = user.getAccountType() == AccountType.STUDENT;

        Introduction introduction = new Introduction();

        UploadedFileWrapper uploadedFileWrapper = new UploadedFileWrapper();

        model.addAttribute("user", user);
        model.addAttribute("isUserStudent", isUserStudent);
        model.addAttribute("project", project);
        model.addAttribute("introduction", introduction);
        model.addAttribute("chapterWrapper", uploadedFileWrapper);

        return "project/phases/introduction";
    }



}
