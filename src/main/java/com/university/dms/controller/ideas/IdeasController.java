package com.university.dms.controller.ideas;

import com.university.dms.Utils.ProjectUtils;
import com.university.dms.model.discussions.Discussion;
import com.university.dms.model.discussions.DiscussionMessage;
import com.university.dms.model.ideas.ProjectIdea;
import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import com.university.dms.service.project.IdeasService;
import com.university.dms.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class IdeasController {

    @Autowired
    private IdeasService ideasService;

    @Autowired
    private UserService userService;


    @RequestMapping("/ideas")
    public String viewIdeasPage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        List<ProjectIdea> ideas = ideasService.findAll();
        ProjectIdea projectIdea = new ProjectIdea();

        model.addAttribute("alreadyHaveTwoIdeasTaken", false);
        model.addAttribute("ideas", ideas);
        model.addAttribute("user", user);
        model.addAttribute("newIdea", projectIdea);

        return "ideas/ideas";
    }

    @PostMapping("/submit-idea")
    public String submitNewIdea(@Valid ProjectIdea projectIdea) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        projectIdea.setPostedBy(user);

        ideasService.save(projectIdea);

        return "redirect:/ideas";
    }

    @RequestMapping("/select-idea/{id}")
    public String takeIdea(Model model, @PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());


        List<ProjectIdea> takenByStudent = ideasService.findProjectIdeaByTakenBy(user);

        if(takenByStudent.size() < 2){
            ProjectIdea projectIdea1 = ideasService.findIdeaById(Integer.parseInt(id));
            projectIdea1.setTakenBy(user);
            ideasService.save(projectIdea1);
            model.addAttribute("alreadyHaveTwoIdeasTaken", false);
        } else {
            model.addAttribute("alreadyHaveTwoIdeasTaken", true);
        }


        List<ProjectIdea> ideas = ideasService.findAll();

        ProjectIdea projectIdea = new ProjectIdea();

        model.addAttribute("ideas", ideas);
        model.addAttribute("user", user);
        model.addAttribute("newIdea", projectIdea);

        return "ideas/ideas";
    }

    @RequestMapping("/deselect-idea/{id}")
    public String deselectIdea(Model model, @PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());


        ProjectIdea takenByStudent = ideasService.findIdeaById(Integer.parseInt(id));
        takenByStudent.setTakenBy(null);

        ideasService.save(takenByStudent);

        model.addAttribute("alreadyHaveTwoIdeasTaken", false);

        List<ProjectIdea> ideas = ideasService.findAll();

        ProjectIdea projectIdea = new ProjectIdea();

        model.addAttribute("ideas", ideas);
        model.addAttribute("user", user);
        model.addAttribute("newIdea", projectIdea);

        return "ideas/ideas";
    }

    @RequestMapping("/delete-idea/{id}")
    public String deleteIdea(Model model, @PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        ideasService.deleteProjectIdeaById(Integer.parseInt(id));

        model.addAttribute("alreadyHaveTwoIdeasTaken", false);

        List<ProjectIdea> ideas = ideasService.findAll();

        ProjectIdea projectIdea = new ProjectIdea();

        model.addAttribute("ideas", ideas);
        model.addAttribute("user", user);
        model.addAttribute("newIdea", projectIdea);

        return "ideas/ideas";
    }



}
