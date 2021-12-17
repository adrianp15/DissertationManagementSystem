package com.university.dms.repository.project;

import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findProjectByStudent(User user);

    Project findProjectById(Integer id);

    Project findProjectBySuggestionId(Integer id);

    @Query("FROM Project p WHERE p.suggestion.title LIKE %:title%")
    List<Project> findBySuggestion_Title(@Param("title") String title);

    @Query("FROM Project p WHERE p.student.firstName LIKE %:name%")
    List<Project> findByStudent_FirstName(@Param("name") String name);

    @Query("FROM Project p WHERE p.student.lastName LIKE %:name%")
    List<Project> findByStudent_LastName(@Param("name") String name);

    @Query("FROM Project p WHERE p.student.email LIKE %:email%")
    List<Project> findByStudent_email(@Param("email") String email);

}
