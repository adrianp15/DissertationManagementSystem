package com.university.dms.model.project;

import com.university.dms.model.user.User;
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

    @Lob
    @Column(name = "document", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] document;

    @Column(name = "grade")
    private String grade;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "marking")
    private ProposalMarking proposalMarking;

}
