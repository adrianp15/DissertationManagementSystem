package com.university.dms.repository.project;

import com.university.dms.model.project.Project;
import com.university.dms.model.project.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

    Suggestion findSuggestionById(Integer id);
}
