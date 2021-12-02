package com.university.dms.model.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "proposal_marking")
public class ProposalMarking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "introduction_mark")
    @Max(value = 20, message = "*Maximum 20 marks")
    @NotNull(message = "*Insert a mark")
    private Integer introductionMark;

    @Column(name = "aims_objectives", columnDefinition = "TEXT")
    private String aimsObjectives;

    @Column(name = "aims_objectives_mark")
    @Max(value = 20, message = "*Maximum 20 marks")
    @NotNull(message = "*Insert a mark")
    private Integer aimsObjectivesMark;

    @Column(name = "methodology", columnDefinition = "TEXT")
    private String methodology;

    @Column(name = "methodology_mark")
    @Max(value = 20, message = "*Maximum 20 marks")
    @NotNull(message = "*Insert a mark")
    private Integer methodologyMark;

    @Column(name = "project_plan", columnDefinition = "TEXT")
    private String projectPlan;

    @Column(name = "project_plan_mark")
    @Max(value = 25, message = "*Maximum 25 marks")
    @NotNull(message = "*Insert a mark")
    private Integer projectPlanMark;

    @Column(name = "summary_conclusion", columnDefinition = "TEXT")
    private String summaryConclusion;

    @Column(name = "summary_conclusion_mark")
    @Max(value = 20, message = "*Maximum 20 marks")
    @NotNull(message = "*Insert a mark")
    private Integer summaryConclusionMark;

    @Column(name = "presentation_appendices", columnDefinition = "TEXT")
    private String presentationAppendices;

    @Column(name = "presentation_appendices_mark")
    @Max(value = 5, message = "*Maximum 20 marks")
    @NotNull(message = "*Insert a mark")
    private Integer presentationAppendicesMark;

}
