package com.university.dms.repository.project;

import com.university.dms.model.discussions.Discussion;
import com.university.dms.model.project.Dissertation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionRepository extends JpaRepository<Discussion, Long> {

    Discussion findDiscussionById(Integer id);
}
