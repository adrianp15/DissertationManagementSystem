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
@Table(name = "dissertation")
public class Dissertation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "literature_review", columnDefinition = "TEXT")
    private String literatureReview;

    @Column(name = "methodology", columnDefinition = "TEXT")
    private String methodology;

    @Column(name = "development", columnDefinition = "TEXT")
    private String development;

    @Column(name = "conclusion", columnDefinition = "TEXT")
    private String conclusion;

    @Column(name = "ethical_form", columnDefinition = "TEXT")
    private String ethical_form;

}
