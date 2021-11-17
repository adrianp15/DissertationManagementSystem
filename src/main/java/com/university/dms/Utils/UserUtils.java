package com.university.dms.Utils;

import com.university.dms.model.AccountType;
import com.university.dms.model.user.User;

public class UserUtils {

    public static boolean isUserCoordinator(User user){
        return user.getAccountType() == AccountType.COORDINATOR;
    }


}
