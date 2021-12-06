package com.university.dms.model.project;


import com.university.dms.model.discussions.Discussion;
import com.university.dms.model.discussions.DiscussionMessage;
import com.university.dms.model.project.enums.DissertationType;
import com.university.dms.model.project.enums.ProjectStatus;
import com.university.dms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "student")
    private User student;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "supervisor")
    private User supervisor;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "suggestion")
    private Suggestion suggestion;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "proposal")
    private Proposal proposal;

    @Column(name="project_type")
    @Enumerated(EnumType.STRING)
    private DissertationType projectType;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "dissertation")
    private Dissertation dissertation;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;

    @Column(name = "preferred_option")
    private Boolean preferredOption;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "project")
    private List<Discussion> discussions;

}
