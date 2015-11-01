package com.tapwisdom.core.daos.apis.impl;

import com.google.common.base.CaseFormat;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.User;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {

    private static final Logger LOG = Logger.getLogger(UserDaoImpl.class);
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static int MAX_NUM_RESULTS = Integer.parseInt(reader.getProperty(Constants.MAX_RES_IN_PAGE, "50"));

    private static final String[] updateParams = {"customProfile", "linkedInProfile", "status",
            "googleProfile", "facebookProfile", "deviceId", "lastSeen", "role", "lastWrittenToIndexedStore",
            "numQuestionsAsked", "numQuestionsAnswered", "visibilityFlag", "aliasName", "mobileOs"};

    /**
     * * Supports updating custom profile, linkedIn profile, email, view count, status, passwordSalt, passwordSha1
     * @param user
     * @return
     */
    @Override
    public Boolean updateUser(User user) throws TapWisdomException {
        if (user.getId() != null) {
            String userId = user.getId();
            LOG.debug("updating user with id: " + userId);
            Update update = new Update();
            if (user.getViewCount() != null && user.getViewCount() != 0) {
                update.inc("viewCount", 1);
            }
//            for (String updateParam : updateParams) {
//                String getterMethodName = "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, updateParam);
//                try {
//                    Method getter = User.class.getMethod(getterMethodName);
//                    Object o = getter.invoke(user);
//                    update.set(updateParam, o);
//                } catch (NoSuchMethodException e) {
//                    LOG.error("Could not update a field: " + e.getMessage(), e);
//                } catch (InvocationTargetException e) {
//                    LOG.error("Could not update a field: " + e.getMessage(), e);
//                } catch (IllegalAccessException e) {
//                    LOG.error("Could not update a field: " + e.getMessage(), e);
//                }
//            }
            if (user.getCustomProfile() != null) {
                update.set("customProfile", user.getCustomProfile());
            }
            if (user.getLinkedInProfile() != null) {
                update.set("linkedInProfile", user.getLinkedInProfile());
            }
            if (user.getStatus() != null) {
                update.set("status", user.getStatus());
            }
            if (user.getGoogleProfile() != null) {
                update.set("googleProfile", user.getGoogleProfile());
            }
            if (user.getFacebookProfile() != null) {
                update.set("facebookProfile", user.getFacebookProfile());
            }
            if (user.getDeviceId() != null) {
                update.set("deviceId", user.getDeviceId());
            }
            if (user.getLastSeen() != null) {
                update.set("lastSeen", user.getLastSeen());
            }
            if (user.getRole() != null) {
                update.set("role", user.getRole());
            }
            if (user.getLastWrittenToIndexedStore() != null) {
                update.set("lastWrittenToIndexedStore", user.getLastWrittenToIndexedStore());
            }
            if (user.getNumQuestionsAsked() != null && user.getNumQuestionsAsked() != 0) {
                update.inc("numQuestionsAsked", 1);
            }
            if (user.getNumQuestionsAnswered() != null && user.getNumQuestionsAnswered() != 0) {
                update.inc("numQuestionsAnswered", 1);
            }
            if (user.getVisibilityFlag() != null) {
                update.set("visibilityFlag", user.getVisibilityFlag());
            }
            if (user.getAliasName() != null) {
                update.set("aliasName", user.getAliasName());
            }
            if (user.getMobileOs() != null) {
                update.set("mobileOs", user.getMobileOs());
            }
            if (user.getQnAUpvoteCount() != null && user.getQnAUpvoteCount() != 0) {
                update.inc("qnAUpvoteCount", 1);
            }
            if (user.getUpvoteBadge() != null) {
                update.set("upvoteBadge", user.getUpvoteBadge());
            }
            if (user.getEvaluateBadge() != null) {
                update.set("evaluateBadge", user.getEvaluateBadge());
            }
            if (user.getTapBadges() != null) {
                update.set("tapBadges", user.getTapBadges());
            }
            if (user.getIsVerifiedAdvisor() != null) {
                update.set("isVerifiedAdviser", user.getIsVerifiedAdvisor());
            }
            if (user.getUpdatedAt() != null) {
                update.set("updatedAt", user.getUpdatedAt());
            }

            try {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(userId)));
                return operations.updateFirst(query, update, User.class).isUpdateOfExisting();
            } catch (org.springframework.dao.DuplicateKeyException e) {
                LOG.error("Could not update a field: " + e.getMessage(), e);
                throw new TapWisdomException(1, "Aliasname is already in use");
            }
        }

        return false;
    }

    @Override
    public User getUser(String id) {
        ObjectId objectId = new ObjectId(id);
        User user = operations.findById(objectId, User.class);
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        User user = operations.findOne(query, User.class);
        return user;
    }

    @Override
    public List<User> getUsersById(List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        List<User> users = operations.find(query, User.class);
        return users;
    }

    @Override
    public List<User> getUsersUpdatedWithinTimeRange(Long timestampAfter, Long timestampBefore) throws TapWisdomException {
        Query query = new Query();
        if (timestampAfter == null) {
            Criteria nullCriteria = Criteria.where("updatedAt").is(null);
            Criteria timeBeforeCriteria = Criteria.where("updatedAt").lt(timestampBefore);
            Criteria criteria = new Criteria().orOperator(nullCriteria, timeBeforeCriteria);
            query.addCriteria(criteria);
        } else {
            Criteria timeAfterCriteria = Criteria.where("updatedAt").gte(timestampAfter);
            Criteria timeBeforeCriteria = Criteria.where("updatedAt").lt(timestampBefore);
            Criteria criteria = new Criteria().andOperator(timeAfterCriteria, timeBeforeCriteria);
            query.addCriteria(criteria);
        }
        List<User> users = operations.find(query, User.class);
        return users;
    }

    @Override
    public List<User> getUsers(int page) {
        Query query = new Query();
        query.with(new PageRequest(page, MAX_NUM_RESULTS));
        List<User> users = operations.find(query, User.class);
        return users;
    }

    @Override
    public List<User> getUsersByCompanyIds(List<String> companyIds, int page) {
        List<ObjectId> companyObjectIds = new ArrayList<ObjectId>();
        for (String companyId : companyIds) {
            companyObjectIds.add(new ObjectId(companyId));
        }
        Query query = new Query(
                Criteria.where("linkedInProfile.positions").elemMatch(Criteria.where("company._id").in(companyObjectIds))
        );
        query.with(new PageRequest(page, MAX_NUM_RESULTS));
        query.with(new Sort(Sort.Direction.DESC, "createdAt"));
        List<User> users = operations.find(query, User.class);
        return users;
    }

}
