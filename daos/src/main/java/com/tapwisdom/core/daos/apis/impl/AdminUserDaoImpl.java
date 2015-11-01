package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.AdminUserDao;
import com.tapwisdom.core.daos.documents.AdminUser;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * Created by srividyak on 02/07/15.
 */
@Component
public class AdminUserDaoImpl extends BaseDaoImpl<AdminUser> implements AdminUserDao {

    @Override
    public AdminUser getAdminUser(String id) throws TapWisdomException {
        return operations.findById(id, AdminUser.class);
    }

    @Override
    public Boolean updateAdminUser(AdminUser user) throws TapWisdomException {
        Query query = new Query();
        Update update = new Update();
        if (user.getId() != null) {
            query.addCriteria(Criteria.where("_id").is(user.getId()));
            if (user.getFirstName() !=  null) {
                update.set("firstName", user.getFirstName());
            }
            if (user.getLastName() != null) {
                update.set("lastName", user.getLastName());
            }
            if (user.getEmail() != null) {
                update.set("email", user.getEmail());
            }
            if (user.getPasswordSalt() != null) {
                update.set("passwordSalt", user.getPasswordSalt());
            }
            if (user.getPasswordSha1() != null) {
                update.set("passwordSha1", user.getPasswordSha1());
            }
            return operations.updateFirst(query, update, AdminUser.class).isUpdateOfExisting();
        } else {
            throw new TapWisdomException(1, "user id is mandatory to update user details");
        }
    }

    @Override
    public AdminUser getAdminUserByEmail(String email) throws TapWisdomException {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));
        return operations.findOne(query, AdminUser.class);
    }
}
