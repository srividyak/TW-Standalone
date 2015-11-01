package com.tapwisdom.core.daos;

import com.google.gson.Gson;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.social.linkedin.api.LinkedInProfile;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by srividyak on 01/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class UserDaoTest extends BaseUserTest {
    
    @Autowired
    private MongoOperations operations;
    
    @Test
    public void testGetUserById() {
        String id = user.getId();
        User retUser = userDao.getUser(id);
        assert retUser != null;
        assert retUser.getId().equals(id);
    }
    
    @Test
    public void testGetUserByEmail() {
        String email = user.getEmail();
        User retUser = userDao.getUserByEmail(email);
        assert retUser != null;
        assert retUser.getEmail().equals(email);
    }
    
    @Test
    public void testUpdateUser() {
        String deviceId = "deviceId_dup";
        User updatedUser = new User();
        user.setId(user.getId());
        user.setDeviceId(deviceId);
        try{
            Boolean updated = userDao.updateUser(user);
            assert updated;
            updatedUser = userDao.getUser(user.getId());
            assert updatedUser.getDeviceId().equals(deviceId);
        }catch (TapWisdomException e){
            assert false;
        }
    }

    @Test
    public void testUniqueAliasNameSuccess() {
        String email = "shravan.s@gmail.com";
        String uniqueAliasName = "uniquealiasname";
        User updateUser = new User();
        updateUser.setEmail(email);
        try {
            userDao.save(updateUser);
        }catch (TapWisdomException e){
            assert false;
        }
        updateUser = userDao.getUserByEmail(email);
        updateUser.setAliasName(uniqueAliasName);
        try {
            userDao.updateUser(updateUser);
            updateUser =userDao.getUserByEmail(email);
            assert updateUser.getAliasName().equals(uniqueAliasName);
        } catch (TapWisdomException e) {
            assert false;
        }

    }

    @Test(expected = TapWisdomException.class)
    public void testUniqueAliasNameFailure() throws TapWisdomException {
        String email = "shravan.s@gmail.com";
        User updateUser = new User();
        updateUser.setEmail(email);
        try {
            userDao.save(updateUser);
        }catch (TapWisdomException e){
            assert false;
        }
        updateUser = userDao.getUserByEmail(email);
        updateUser.setAliasName("userAliasName");
        userDao.updateUser(updateUser);
    }
    
    @Test
    public void testGetUsersById() {
        // create one more user
        User testUser = new User();
        testUser.setEmail("vidya@gmail.com");
        try {
            userDao.save(testUser);
            List<User> users = userDao.getUsersById(Arrays.asList(testUser.getId(), user.getId()));
            assert users != null;
            assert users.size() == 2;
            userDao.delete(testUser);
        } catch (TapWisdomException e) {
            assert false;
        }
    }
    
    @Test
    public void testGetUsersUpdatedWithinTimeRangeInclusive() {
        String[] emails = {"vidya@gmail.com", "vidya@yahoo.com"};
        Long timestamp = new Date().getTime();
        Long[] timestamps = {timestamp, timestamp + 1000L};
        List<User> users = new ArrayList<User>();
        int index = 0;
        for (String email : emails) {
            User testUser = new User();
            testUser.setEmail(email);
            testUser.setUpdatedAt(timestamps[index]);
            users.add(testUser);
            index++;
        }
        try {
            for (User user : users) {
                operations.save(user);
            }
            List<User> usersWithinRange = userDao.getUsersUpdatedWithinTimeRange(timestamps[0], timestamps[1] + 1000L);
            assert usersWithinRange != null;
            assert usersWithinRange.size() == 2;
            assert usersWithinRange.get(0).getEmail().equals(emails[0]);
            assert usersWithinRange.get(1).getEmail().equals(emails[1]);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetUsersUpdatedWithinRangeExclusive() {
        String[] emails = {"vidya@gmail.com", "vidya@yahoo.com"};
        Long timestamp = new Date().getTime();
        Long[] timestamps = {timestamp, timestamp + 1000L};
        List<User> users = new ArrayList<User>();
        int index = 0;
        for (String email : emails) {
            User testUser = new User();
            testUser.setEmail(email);
            testUser.setUpdatedAt(timestamps[index]);
            users.add(testUser);
            index++;
        }
        try {
            for (User user : users) {
                operations.save(user);
            }
            List<User> usersWithinRange = userDao.getUsersUpdatedWithinTimeRange(timestamps[0], timestamps[1]);
            assert usersWithinRange != null;
            assert usersWithinRange.size() == 1;
            assert usersWithinRange.get(0).getEmail().equals(emails[0]);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetUsersUpdatedWithinRangeWithNullAfter() {
        String[] emails = {"vidya@gmail.com", "vidya@yahoo.com"};
        Long timestamp = new Date().getTime();
        Long[] timestamps = {timestamp, timestamp + 1000L};
        List<User> users = new ArrayList<User>();
        int index = 0;
        for (String email : emails) {
            User testUser = new User();
            testUser.setEmail(email);
            testUser.setUpdatedAt(timestamps[index]);
            users.add(testUser);
            index++;
        }
        try {
            for (User user : users) {
                operations.save(user);
            }
            List<User> usersWithinRange = userDao.getUsersUpdatedWithinTimeRange(null, timestamps[1] + 1000L);
            assert usersWithinRange != null;
            assert usersWithinRange.size() == 3;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetUsersByCompany() {
        try {
            // modify liProfile
            String companyId = "55d21327214a9f111e9f72e2";
            LiProfile liProfile = user.getLinkedInProfile();
            Position position = new Position();
            Company company = new Company();
            company.setName("tapwisdom");
            company.setId(companyId);
            position.setCompany(company);
            liProfile.setPositions(Arrays.asList(position));
            userDao.save(user);
            
            List<User> users = userDao.getUsersByCompanyIds(Arrays.asList(companyId), 0);
            assert users.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
}
