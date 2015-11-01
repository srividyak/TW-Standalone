package com.tapwisdom.core.daos.documents;

import com.google.common.base.CaseFormat;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by srividyak on 03/07/15.
 */
public class AdminUserViewable extends BaseEntity {

    private String firstName;
    private String lastName;
    private String email;
    private UserStatus userStatus = UserStatus.active;
    
    private static final Logger LOG = Logger.getLogger(AdminUserViewable.class);

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
    
}
