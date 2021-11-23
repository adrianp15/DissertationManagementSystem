package com.university.dms.service.project;

import com.university.dms.model.project.Suggestion;
import com.university.dms.repository.project.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;

    @Autowired
    public SuggestionService(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }

    public Suggestion findSuggestionById(Integer id) {
        return suggestionRepository.findSuggestionById(id);
    }

    public void saveSuggestion(Suggestion suggestion) {
        suggestionRepository.save(suggestion);
    }
}
