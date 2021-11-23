package com.university.dms.repository.project;

import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findProjectByStudent(User user);

    Project findProjectById(Integer id);


}
