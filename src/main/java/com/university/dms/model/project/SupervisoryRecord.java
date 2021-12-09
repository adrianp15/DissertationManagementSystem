package com.university.dms.model.project;

import com.university.dms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "supervisory_record")
public class SupervisoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "student")
    private User student;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "supervisor")
    private User supervisor;

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @Column(name = "contact_format")
    private String contactFormat;

    @Column(name = "student_attendance")
    private String studentAttendance;

    @Column(name = "reason_student_not_attending")
    private String reasonStudentNotAttending;

    @Column(name = "student_complete_agreed_points")
    private String studentCompleteAgreedPoints;

    @Column(name = "points_coved_in_meeting")
    private String pointsCoveredInMeeting;

    @Column(name = "agreed_points_from_meeting")
    private String agreedPointsFromMeeting;

    @Column(name = "student_commitment")
    private Integer studentCommitment;

    @Column(name = "concerns")
    private String concerns;
}
