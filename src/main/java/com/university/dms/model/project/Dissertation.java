package com.university.dms.model.project;

import com.university.dms.model.project.dissertationchapters.*;
import com.university.dms.model.project.enums.ChapterTaskFeedback;
import com.university.dms.model.project.enums.DissertationType;
import com.university.dms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "introduction")
    private Introduction introduction;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "literature_review")
    private LiteratureReview literatureReview;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "methodology")
    private Methodology methodology;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "development_testing")
    private DevelopmentTesting developmentTesting;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "conclusion")
    private Conclusion conclusion;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "presentation_references")
    private PresentationReferences presentationReferences;

}
