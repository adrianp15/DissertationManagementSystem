package com.university.dms.repository.project;

import com.university.dms.model.forum.ForumTopic;
import com.university.dms.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumRepository extends JpaRepository<ForumTopic, Integer> {
    List<ForumTopic> findAll();

    ForumTopic findForumTopicById(Integer id);

}
