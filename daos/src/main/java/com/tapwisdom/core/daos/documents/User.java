package com.tapwisdom.core.daos.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.social.linkedin.api.LinkedInProfileFull;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by srividyak on 18/04/15.
 */
@Document(collection = "user")
public class User extends UserView implements Serializable {

    private String passwordSalt;
    private String passwordSha1;

    public User() {
        super();
    }
    
    private User(boolean isEmpty) {
        super(true);
    }
    
    public static User buildEmptyUser() {
        return new User(true);
    }

    @Indexed
    private Long lastWrittenToIndexedStore;

    public Long getLastWrittenToIndexedStore() {
        return lastWrittenToIndexedStore;
    }

    public void setLastWrittenToIndexedStore(Long lastWrittenToIndexedStore) {
        this.lastWrittenToIndexedStore = lastWrittenToIndexedStore;
    }

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
