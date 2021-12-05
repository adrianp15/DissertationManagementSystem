package com.university.dms.model.project.dissertationchapters;

import com.university.dms.model.project.enums.ChapterStatus;
import com.university.dms.model.project.enums.ChapterTaskFeedback;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dissertation_methodology")
public class Methodology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "project_id")
    private String projectId;

    @Lob
    @Column(name = "submitted_document", columnDefinition = "LONGBLOB")
    private byte[] submittedDocument;

    @Column(name="chapter_status")
    @Enumerated(EnumType.STRING)
    private ChapterStatus chapterStatus;

    @Column(name="introduction_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback introductionSubtask;

    @Column(name="justification_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback justificationSubtask;

    @Column(name="methodology_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback methodologySubtask;

    @Column(name="planning_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback planningSubtask;

    @Column(name="summary_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback summarySubtask;

    @Column(name = "supervisor_feedback", columnDefinition = "TEXT")
    private String supervisorFeedback;

}
