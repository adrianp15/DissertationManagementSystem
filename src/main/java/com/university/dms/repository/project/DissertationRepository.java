package com.university.dms.repository.project;

import com.university.dms.model.project.Dissertation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DissertationRepository extends JpaRepository<Dissertation, Long> {
}
