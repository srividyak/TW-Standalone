package com.tapwisdom.core.es;

import com.tapwisdom.core.daos.documents.UserView;
import com.tapwisdom.core.es.documents.User;

public class UserBuilder {
    
    public static User buildUser(UserView userView) {
        User user = new User();
        user.setUserView(userView);
        return user;
    }
    
}
