package com.university.dms.model.forum;

import com.university.dms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "forum_post")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "post_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime postDate;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @ManyToOne
    private ForumTopic topic;
}
