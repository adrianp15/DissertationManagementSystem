package com.university.dms.service.project;

import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import com.university.dms.repository.project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
}
