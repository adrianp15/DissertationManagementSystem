package com.university.dms.service.project;

import com.university.dms.model.forum.ForumTopic;
import com.university.dms.repository.project.ForumRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumService {

    private final ForumRepository forumRepository;

    public ForumService(ForumRepository forumRepository) {
        this.forumRepository = forumRepository;
    }

    public void saveTopic(ForumTopic forumTopic) {
        forumRepository.save(forumTopic);
    }

    public List<ForumTopic> findAll(){
        return forumRepository.findAll();
    }

    public ForumTopic findTopicById(Integer id) {
        return forumRepository.findForumTopicById(id);
    }

}
