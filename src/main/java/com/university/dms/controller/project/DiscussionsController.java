package com.university.dms.controller.project;

import com.university.dms.Utils.ProjectUtils;
import com.university.dms.model.discussions.Discussion;
import com.university.dms.model.discussions.DiscussionMessage;
import com.university.dms.model.project.Project;
import com.university.dms.model.project.enums.ProjectStatus;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.DiscussionWrapper;
import com.university.dms.model.utils.NewMessageWrapper;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class DiscussionsController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/submit-new-discussion")
    public String submitNewDiscussion(@Valid DiscussionWrapper discussionWrapper, Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(discussionWrapper.getProjectId());

        Discussion discussion = new Discussion();
        discussion.setTitle(discussionWrapper.getTitle());

        DiscussionMessage discussionMessage = new DiscussionMessage();
        discussionMessage.setMessage(discussionWrapper.getInitialMessage());
        discussionMessage.setPostedBy(user);
        discussionMessage.setPostDate(LocalDateTime.now());

        MultipartFile file = discussionWrapper.getDocument();

        byte[] result = ProjectUtils.convertWordToPDF(file);

        discussionMessage.setAttachment(result);
        discussionMessage.setDiscussion(discussion);

        if(discussion.getMessages() == null){
            discussion.setMessages(new ArrayList<>());
        }

        discussion.getMessages().add(discussionMessage);

        discussion.setProject(project);

        project.getDiscussions().add(discussion);

        projectService.saveProject(project);

        return "redirect:/projects/" + project.getId();
    }

    @GetMapping(value = "/projects/{id}/{discussionid}")
    public String viewDiscussionPage(Model model, @PathVariable("id") String id, @PathVariable("discussionid") String discussionid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Project project = projectService.findProjectById(Integer.parseInt(id));

        List<Discussion> discussions = project.getDiscussions();
        Discussion discussion = null;

        for(Discussion discussion1 : discussions){
            if(discussion1.getId() == Integer.parseInt(discussionid)){
                discussion = discussion1;
            }
        }

        NewMessageWrapper newMessageWrapper = new NewMessageWrapper();

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("discussion", discussion);
        model.addAttribute("newMessageWrapper", newMessageWrapper);

        return "project/discussionpage";
    }

    @GetMapping("/getmessagedocument/{message}/{id}")
    public ResponseEntity<byte[]> getMessageAttachedDocument(@PathVariable("id") String id, @PathVariable("message") String message, HttpServletResponse resp) throws IOException, SQLException {

        Project project = projectService.findProjectById(Integer.parseInt(id));

        byte[] byteArray = new byte[0];

        for(Discussion discussion : project.getDiscussions()){
            for(DiscussionMessage discussionMessage : discussion.getMessages()){
                if(discussionMessage.getId() == Integer.parseInt(message)){
                    byteArray = discussionMessage.getAttachment();
                }
            }
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

    @PostMapping("/submit-new-message")
    public String postNewMessage(@Valid NewMessageWrapper newUserMessageWrapper, Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(newUserMessageWrapper.getProjectId());

        Discussion discussion = projectService.findDiscussionById(newUserMessageWrapper.getDiscussionId());

        DiscussionMessage discussionMessage = new DiscussionMessage();

        discussionMessage.setMessage(newUserMessageWrapper.getMessage());
        discussionMessage.setPostedBy(user);
        discussionMessage.setPostDate(LocalDateTime.now());

        MultipartFile file = newUserMessageWrapper.getDocument();

        byte[] result = ProjectUtils.convertWordToPDF(file);

        discussionMessage.setAttachment(result);
        discussionMessage.setDiscussion(discussion);

        if(discussion.getMessages() == null){
            discussion.setMessages(new ArrayList<>());
        }

        discussion.getMessages().add(discussionMessage);

        discussion.setProject(project);

        project.getDiscussions().add(discussion);

        projectService.saveProject(project);

        NewMessageWrapper newMessageWrapper = new NewMessageWrapper();

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("discussion", discussion);
        model.addAttribute("newMessageWrapper", newMessageWrapper);

        return "project/discussionpage";
    }

}
