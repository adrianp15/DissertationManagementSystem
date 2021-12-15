package com.university.dms.service.project;

import com.university.dms.model.ideas.ProjectIdea;
import com.university.dms.model.user.User;
import com.university.dms.repository.project.IdeasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IdeasService {

    @Autowired
    private IdeasRepository ideasRepository;

    public List<ProjectIdea> findAll(){
        return ideasRepository.findAll();
    }

    public void save(ProjectIdea projectIdea) {
        ideasRepository.save(projectIdea);
    }

    public ProjectIdea findIdeaById(Integer id) {
        return ideasRepository.findProjectIdeaById(id);
    }

    public List<ProjectIdea> findProjectIdeaByTakenBy(User user) {
        return ideasRepository.findProjectIdeaByTakenBy(user);
    }

    @Transactional
    public void deleteProjectIdeaById(Integer id){
        ideasRepository.deleteProjectIdeaById(id);
    }
}
