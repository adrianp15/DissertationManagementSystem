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
@Table(name = "dissertation_development")
public class DevelopmentTesting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "submitted_document", columnDefinition = "LONGBLOB")
    private byte[] submittedDocument;

    @Column(name="chapter_status")
    @Enumerated(EnumType.STRING)
    private ChapterStatus chapterStatus;

    @Column(name="introduction_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback introductionSubtask;

    @Column(name="specifications_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback specificationsSubtask;

    @Column(name="analysis_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback analysisSubtask;

    @Column(name="design_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback designSubtask;

    @Column(name="development_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback developmentSubtask;

    @Column(name="testing_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback testingSubtask;

    @Column(name="challenges_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback challengesSubtask;

    @Column(name="compare_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback compareSubtask;

    @Column(name="problems_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback problemsSubtask;

    @Column(name="reflect_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback reflectSubtask;

    @Column(name="discussion_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback discussionSubtask;

    @Column(name="summary_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback summarySubtask;

    @Column(name = "supervisor_feedback", columnDefinition = "TEXT")
    private String supervisorFeedback;

}
