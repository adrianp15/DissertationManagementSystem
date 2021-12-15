package com.university.dms.controller.meetings;

import com.university.dms.model.AccountType;
import com.university.dms.model.meetings.Meeting;
import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import com.university.dms.model.utils.MeetingWrapper;
import com.university.dms.service.project.MeetingsService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Controller
public class MeetingsController {

    @Autowired
    private MeetingsService meetingsService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;


    @GetMapping(value = "/{id}/meetingspage")
    public String viewMeetingsPage(Model model,  @PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(id));

        List<Meeting> meetings1;
        List<Meeting> meetings2;

        if(user.getAccountType() == AccountType.COORDINATOR){
            meetings1 = meetingsService.findAllByStudent(project.getStudent());
            meetings2 = meetingsService.findAllBySupervisor(project.getSupervisor());
        } else {
            meetings1 = meetingsService.findAllByStudent(user);
            meetings2 = meetingsService.findAllBySupervisor(user);
        }

        Set<Meeting> meetings = new HashSet<>();
        meetings.addAll(meetings1);
        meetings.addAll(meetings2);

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("meetings", meetings);
        return "project/meetingspage";
    }

    @GetMapping(value = "/{id}/new-meeting")
    public String newMeeting(Model model,  @PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(id));

        Meeting meeting = new Meeting();

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("meeting", meeting);
        return "project/newmeeting";
    }

    @PostMapping(value = "/submitnewmeeting")
    public String submitNewMeeting(@Valid @ModelAttribute("meeting") Meeting meeting, BindingResult result, ModelMap model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Project project = projectService.findProjectById(Integer.parseInt(meeting.getProjectId()));

        if(meeting.getDate() == null || StringUtils.isBlank(meeting.getTime()) || meeting.getDuration() <= 0){
            model.addAttribute("user", user);
            model.addAttribute("project", project);
            model.addAttribute("meeting", meeting);
            return "project/newmeeting";
        }

        if(Objects.equals(user, project.getStudent())){
            meeting.setStudent(user);
            meeting.setSupervisor(project.getSupervisor());
        } else {
            meeting.setStudent(project.getStudent());
            meeting.setSupervisor(user);
        }

        meetingsService.saveMeeting(meeting);

        return "redirect:/" + project.getId() + "/meetingspage";
    }

    @GetMapping(value = "/{id}/delete-meeting")
    public String deleteMeeting(Model model,  @PathVariable("id") String id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        String projectId = meetingsService.findMeetingByID(Integer.parseInt(id)).getProjectId();
        meetingsService.deleteMeetingByID(Integer.parseInt(id));

        return "redirect:/" + projectId + "/meetingspage";
    }

}
