package com.tapwisdom.core.daos;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.FacebookProfile;
import com.tapwisdom.core.daos.documents.GoogleProfile;
import com.tapwisdom.core.daos.documents.LiProfile;
import com.tapwisdom.core.daos.documents.User;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.social.linkedin.api.LinkedInProfileFull;

import java.util.Date;

/**
 * Created by srividyak on 02/07/15.
 */
public class BaseUserTest {

    @Autowired
    protected UserDao userDao;

    protected User user;

    @Autowired
    private MongoOperations operations;

    @Before
    public void createUser() {
        user = new User();
        GoogleProfile googleProfile = new GoogleProfile();
        googleProfile.setEmail("vidya.vasishtha5@gmail.com");
        googleProfile.setProfileImage("googlePictureUrl");
        googleProfile.setProfileUrl("googleProfileUrl");
        googleProfile.setFirstName("srividya");
        googleProfile.setLastName("krishnamurthy");

        FacebookProfile facebookProfile = new FacebookProfile();
        facebookProfile.setEmail("vidya.vasishtha5@yahoo.com");
        facebookProfile.setProfileImage("fbPictureUrl");
        facebookProfile.setProfileUrl("fbProfileUrl");
        facebookProfile.setName("vidya");

        LiProfile linkedInProfile = new LiProfile();
        linkedInProfile.setFirstName("Srividya");
        linkedInProfile.setLastName("Krishnamurthy");

        user.setLinkedInProfile(linkedInProfile);
        user.setGoogleProfile(googleProfile);
        user.setFacebookProfile(facebookProfile);

        user.setDeviceId("deviceId");
        user.setEmail("vidya.vasishtha5@gmail.com");
        user.setAliasName("userAliasName");
        user.setLastSeen(new Date().getTime());
        try {
            userDao.save(user);
        } catch (TapWisdomException e) {
            assert false;
        }
    }

    @After
    public void deleteUser() {
        Query query = new Query();
        operations.findAllAndRemove(query, User.class);
    }
}
