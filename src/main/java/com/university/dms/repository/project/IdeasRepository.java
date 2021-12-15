package com.university.dms.repository.project;

import com.university.dms.model.ideas.ProjectIdea;
import com.university.dms.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IdeasRepository extends JpaRepository<ProjectIdea, Integer> {

    ProjectIdea findProjectIdeaById(Integer id);

    List<ProjectIdea> findProjectIdeaByTakenBy(User user);

    @Transactional
    void deleteProjectIdeaById(Integer id);
}
