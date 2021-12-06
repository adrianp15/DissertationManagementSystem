package com.university.dms.model.discussions;

import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discussions")
public class Discussion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "discussion")
    private List<DiscussionMessage> messages;

    @ManyToOne
    private Project project;

}
