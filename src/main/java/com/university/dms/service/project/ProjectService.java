package com.university.dms.service.project;

import com.university.dms.model.project.Dissertation;
import com.university.dms.model.project.Project;
import com.university.dms.model.project.enums.ProjectStatus;
import com.university.dms.model.project.Proposal;
import com.university.dms.model.user.User;
import com.university.dms.repository.project.DissertationRepository;
import com.university.dms.repository.project.ProjectRepository;
import com.university.dms.repository.project.ProposalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProposalRepository proposalRepository;

    private final DissertationRepository dissertationRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, ProposalRepository proposalRepository, DissertationRepository dissertationRepository) {
        this.projectRepository = projectRepository;
        this.proposalRepository = proposalRepository;
        this.dissertationRepository = dissertationRepository;
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public List<Project> getCurrentStudentProjects(User user) {
        return projectRepository.findProjectByStudent(user);
    }

    public void setPreferredProject(User user, Integer projectId){
        List<Project> projects = projectRepository.findProjectByStudent(user);

        for(Project project:projects) {
            if(project.getPreferredOption()) {
                project.setPreferredOption(false);
                projectRepository.save(project);
            }

            if (Objects.equals(project.getId(), projectId)) {
                project.setPreferredOption(true);
                projectRepository.save(project);
            }
        }
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findProjectById(Integer id) {
        return projectRepository.findProjectById(id);
    }

    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    public void setSupervisor(Project project) {
        List<Project> studentProjects = projectRepository.findProjectByStudent(project.getStudent());
        for(Project project1: studentProjects) {
            if (Objects.equals(project1.getId(), project.getId())) {
                projectRepository.save(project);
            } else {
                project1.setSupervisor(null);
                project1.setProjectStatus(ProjectStatus.SUGGESTION_REJECTED);
                projectRepository.save(project1);
            }
        }
    }

    public List<Project> searchProject(String keyword) {
        Set<Project> projects = new HashSet<>();

        List<Project> results = projectRepository.findBySuggestion_Title(keyword);

        if(results != null) {
            projects.addAll(results);
        }

        results = projectRepository.findByStudent_FirstName(keyword);

        if(results != null) {
            projects.addAll(results);
        }

        results = projectRepository.findByStudent_LastName(keyword);

        if(results != null) {
            projects.addAll(results);
        }

        results = projectRepository.findByStudent_email(keyword);

        if(results != null) {
            projects.addAll(results);
        }
        results = new ArrayList<>(projects);

        return results;
    }

    public boolean hasUserAnyProject(User user) {
        List<Project> projects = projectRepository.findAll();

        for(Project project : projects){
            if(Objects.equals(project.getStudent(), user)){
                return true;
            }
        }
        return false;
    }

    public void saveProposal(Proposal proposal) {
        proposalRepository.save(proposal);
    }

    public Proposal findProposalById(Integer id){
        return proposalRepository.findProposalById(id);
    }

    public List<Project> getSupervisorProjects(User user) {
        List<Project> allProjects = projectRepository.findAll();
        List<Project> supervisorProjects = new ArrayList<>();

        for(Project project:allProjects) {
            if(Objects.equals(project.getSupervisor(), user )){
                supervisorProjects.add(project);
            }
        }

        return supervisorProjects;

    }

    public void saveDissertation(Dissertation dissertation){
        dissertationRepository.save(dissertation);
    }

}
