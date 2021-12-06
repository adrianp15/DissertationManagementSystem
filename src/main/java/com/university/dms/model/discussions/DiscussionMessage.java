package com.university.dms.model.discussions;

import com.university.dms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "discussion_messages")
public class DiscussionMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Lob
    @Column(name = "attachment", columnDefinition = "LONGBLOB")
    private byte[] attachment;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "post_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    private LocalDateTime postDate;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @ManyToOne
    private Discussion discussion;



}
