package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by srividyak on 02/07/15.
 */
@Document(collection = "admin_user")
public class AdminUser extends AdminUserViewable {
    
    private String passwordSalt;
    private String passwordSha1;
    
    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordSha1() {
        return passwordSha1;
    }

    public void setPasswordSha1(String passwordSha1) {
        this.passwordSha1 = passwordSha1;
    }
    
}
