package com.university.dms.model.project;

import com.university.dms.model.project.enums.DissertationType;
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
@Table(name = "suggestions")
public class Suggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "methodology", columnDefinition = "TEXT")
    private String methodology;

    @Column(name = "deliverables", columnDefinition = "TEXT")
    private String deliverables;

    @Column(name = "lsep", columnDefinition = "TEXT")
    private String lsep;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name="project_type")
    @Enumerated(EnumType.STRING)
    private DissertationType projectType;




}
