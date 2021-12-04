package com.university.dms.model.project.dissertationchapters;

import com.university.dms.model.project.enums.ChapterTaskFeedback;
import com.university.dms.model.project.enums.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dissertation_introduction")
public class Introduction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "submitted_document", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] submittedDocument;

    @Column(name="abstract_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback abstractSubtask;

    @Column(name="introduction_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback introductionSubtask;

    @Column(name="scope_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback scopeSubtask;

    @Column(name="approach_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback approachSubtask;

    @Column(name="overview_subtask")
    @Enumerated(EnumType.STRING)
    private ChapterTaskFeedback overviewSubtask;

    @Column(name = "supervisor_feedback", columnDefinition = "TEXT")
    private String supervisorFeedback;


}
