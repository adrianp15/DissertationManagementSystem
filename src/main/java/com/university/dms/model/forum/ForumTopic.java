package com.university.dms.model.forum;

import com.university.dms.model.discussions.DiscussionMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "forum_topic")
public class ForumTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "topic")
    private List<ForumPost> posts;

}
