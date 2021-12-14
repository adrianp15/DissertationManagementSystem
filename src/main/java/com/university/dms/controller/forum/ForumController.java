package com.university.dms.controller.forum;

import com.university.dms.Utils.ProjectUtils;
import com.university.dms.model.discussions.Discussion;
import com.university.dms.model.discussions.DiscussionMessage;
import com.university.dms.model.forum.ForumPost;
import com.university.dms.model.forum.ForumTopic;
import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.DiscussionWrapper;
import com.university.dms.model.utils.ForumPostWrapper;
import com.university.dms.model.utils.ForumTopicWrapper;
import com.university.dms.model.utils.NewMessageWrapper;
import com.university.dms.service.project.ForumService;
import com.university.dms.service.project.ProjectService;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ForumController {

    @Autowired
    private UserService userService;

    @Autowired
    private ForumService forumService;


    @GetMapping("/forum")
    public String viewForum(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findUserByUserName(auth.getName());

        List<ForumTopic> forumTopics = forumService.findAll();
        ForumTopicWrapper forumTopicWrapper = new ForumTopicWrapper();

        model.addAttribute("user", currentUser);
        model.addAttribute("forumTopics", forumTopics);
        model.addAttribute("forumTopicWrapper", forumTopicWrapper);

        return "forum/forum";
    }

    @PostMapping("/submit-new-forum-topic")
    public String submitNewForumTopic(@Valid ForumTopicWrapper forumTopicWrapper, Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        ForumTopic forumTopic = new ForumTopic();

        forumTopic.setTitle(forumTopicWrapper.getTitle());

        ForumPost forumPost = new ForumPost();
        forumPost.setMessage(forumTopicWrapper.getInitialMessage());
        forumPost.setPostedBy(user);
        forumPost.setPostDate(LocalDateTime.now());
        forumPost.setTopic(forumTopic);

        if(forumTopic.getPosts() == null){
            forumTopic.setPosts(new ArrayList<>());
        }

        forumTopic.getPosts().add(forumPost);

        forumService.saveTopic(forumTopic);

        return "redirect:/forum";
    }

    @GetMapping(value = "/forum/{id}")
    public String viewForumTopic(Model model, @PathVariable("id") String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        ForumTopic forumTopic = forumService.findTopicById(Integer.parseInt(id));

        ForumPostWrapper forumPostWrapper = new ForumPostWrapper();

        model.addAttribute("user", user);
        model.addAttribute("forumPostWrapper", forumPostWrapper);
        model.addAttribute("forumTopic", forumTopic);

        return "forum/forumtopic";
    }

    @PostMapping("/submit-new-forum-post")
    public String postNewForumPost(@Valid ForumPostWrapper newForumPostWrapper, Model model) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        ForumTopic forumTopic = forumService.findTopicById(newForumPostWrapper.getTopicId());

        ForumPost forumPost = new ForumPost();
        forumPost.setMessage(newForumPostWrapper.getMessage());
        forumPost.setPostDate(LocalDateTime.now());
        forumPost.setPostedBy(user);
        forumPost.setTopic(forumTopic);

        if(forumTopic.getPosts() == null){
            forumTopic.setPosts(new ArrayList<>());
        }

        forumTopic.getPosts().add(forumPost);

        forumService.saveTopic(forumTopic);

        ForumPostWrapper forumPostWrapper = new ForumPostWrapper();

        model.addAttribute("user", user);
        model.addAttribute("forumPostWrapper", forumPostWrapper);
        model.addAttribute("forumTopic", forumTopic);

        return "forum/forumtopic";
    }

}
