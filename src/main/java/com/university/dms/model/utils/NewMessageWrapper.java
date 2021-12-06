package com.university.dms.model.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewMessageWrapper {

    private Integer projectId;

    private Integer discussionId;

    private MultipartFile document;

    private String message;
}
