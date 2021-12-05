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
@Table(name = "dissertation_conclusion")
public class Conclusion {

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

    @Column(name="summary_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback summarySubtask;

    @Column(name="conclusions_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback conclusionsSubtask;

    @Column(name="recommendations_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback recommendationsSubtask;

    @Column(name="future_work_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback futureWorkSubtask;

    @Column(name = "supervisor_feedback", columnDefinition = "TEXT")
    private String supervisorFeedback;
}
