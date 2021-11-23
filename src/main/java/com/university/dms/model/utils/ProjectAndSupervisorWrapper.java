package com.university.dms.model.utils;

import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAndSupervisorWrapper {

    private Project project;

    private User supervisor;

}
