package com.university.dms.repository.project;

import com.university.dms.model.meetings.Meeting;
import com.university.dms.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findAllByStudent(User user);

    List<Meeting> findAllBySupervisor(User user);


}
