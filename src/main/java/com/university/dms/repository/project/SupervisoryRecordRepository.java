package com.university.dms.repository.project;

import com.university.dms.model.project.Suggestion;
import com.university.dms.model.project.SupervisoryRecord;
import com.university.dms.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupervisoryRecordRepository extends JpaRepository<SupervisoryRecord, Long> {
    List<SupervisoryRecord> findAllBySupervisor(User user);

    SupervisoryRecord findSupervisoryRecordById(Integer id);

    List<SupervisoryRecord> findAllByProjectId(String id);
}
