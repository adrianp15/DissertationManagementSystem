package com.university.dms.model.meetings;

import com.university.dms.model.project.enums.DissertationType;
import com.university.dms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "meetings")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "time")
    private String time;

    @Column(name = "duration")
    private int duration;

    @Column(name = "meeting_type")
    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Column(name = "meeting_platform")
    @Enumerated(EnumType.STRING)
    private MeetingPlatform meetingPlatform;

    @Column(name = "meeting_link", columnDefinition = "TEXT")
    private String meetingLink;

    @Column(name = "other_details", columnDefinition = "TEXT")
    private String otherDetails;

    @Column(name = "project_id")
    private String projectId;

    @ManyToOne
    private User student;

    @ManyToOne
    private User supervisor;
}

