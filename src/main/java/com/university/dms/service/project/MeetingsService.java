package com.university.dms.service.project;

import com.university.dms.model.meetings.Meeting;
import com.university.dms.model.user.User;
import com.university.dms.repository.project.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MeetingsService {

    @Autowired
    MeetingRepository meetingRepository;

    public List<Meeting> findAllByStudent(User user) {
        return meetingRepository.findAllByStudent(user);
    }

    public List<Meeting> findAllBySupervisor(User user) {
        return meetingRepository.findAllBySupervisor(user);
    }

    public void saveMeeting(Meeting meeting){
        meetingRepository.save(meeting);
    }

    public Meeting findMeetingByID(Integer id){
        return meetingRepository.findMeetingById(id);
    }

    @Transactional
    public void deleteMeetingByID(Integer id){
        meetingRepository.deleteMeetingById(id);
    }

}
