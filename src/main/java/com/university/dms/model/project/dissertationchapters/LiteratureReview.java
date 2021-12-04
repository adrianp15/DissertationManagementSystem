package com.university.dms.model.project.dissertationchapters;

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
@Table(name = "dissertation_literature_review")
public class LiteratureReview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "submitted_document", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] submittedDocument;

    @Column(name="introduction_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback introductionSubtask;

    @Column(name="overview_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback overviewSubtask;

    @Column(name="concerns_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback concernsSubtask;

    @Column(name="requirements_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback requirementsSubtask;

    @Column(name="lit_review_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback litReviewSubtask;

    @Column(name="lsep_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback lsepSubtask;

    @Column(name="summary_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback summarySubtask;

    @Column(name = "supervisor_feedback", columnDefinition = "TEXT")
    private String supervisorFeedback;
}
