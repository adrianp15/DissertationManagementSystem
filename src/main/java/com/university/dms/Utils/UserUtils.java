package com.university.dms.Utils;

import com.university.dms.DissertationManagementSystem;
import com.university.dms.model.AccountType;
import com.university.dms.model.project.Project;
import com.university.dms.model.user.User;
import com.university.dms.repository.project.ProjectRepository;
import com.university.dms.service.project.ProjectService;

import java.util.List;
import java.util.Objects;

public class UserUtils {

    public static boolean isUserCoordinator(User user){
        return user.getAccountType() == AccountType.COORDINATOR;
    }

    public static boolean hasUSerProjects(User user, List<Project> projects ){

        for(Project project : projects){
            if(Objects.equals(project.getStudent(), user)){
                return true;
            }
        }
        return false;
    }


}
