package com.university.dms.Utils;

import com.university.dms.model.AccountType;
import com.university.dms.model.User;
import com.university.dms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtils {

    public static boolean isUserCoordinator(User user){
        return user.getAccountType() == AccountType.COORDINATOR;
    }


}
