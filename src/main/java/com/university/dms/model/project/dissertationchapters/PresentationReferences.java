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
@Table(name = "dissertation_presentation_references")
public class PresentationReferences {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "submitted_document", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] submittedDocument;

    @Column(name="report_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback reportSubtask;

    @Column(name="front_section_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback frontSectionSubtask;

    @Column(name="discussion_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback discussionSubtask;

    @Column(name="language_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback languageSubtask;

    @Column(name="referencing_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback referencingSubtask;

    @Column(name="appendix_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback appendixSubtask;

    @Column(name="other_docs_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback otherDocsSubtask;

    @Column(name = "supervisor_feedback", columnDefinition = "TEXT")
    private String supervisorFeedback;

}
