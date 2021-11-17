package com.university.dms.model.project;

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
@Table(name = "proposal")
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "aims_and_objectives", columnDefinition = "TEXT")
    private String aimsAndObjectives;

    @Column(name = "methodology", columnDefinition = "TEXT")
    private String methodology;

    @Column(name = "project_plan", columnDefinition = "TEXT")
    private String project_plan;

    @Column(name = "conclusion", columnDefinition = "TEXT")
    private String conclusion;

    @Column(name = "references", columnDefinition = "TEXT")
    private String references;

    @Column(name = "ethical_form", columnDefinition = "TEXT")
    private String ethical_form;

}
