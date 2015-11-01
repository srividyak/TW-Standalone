package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.AdminUser;

/**
 * Created by srividyak on 02/07/15.
 */
public interface AdminUserDao extends BaseDao<AdminUser> {

    public AdminUser getAdminUser(String id) throws TapWisdomException;
    
    public Boolean updateAdminUser(AdminUser user) throws TapWisdomException;
    
    public AdminUser getAdminUserByEmail(String email) throws TapWisdomException;

}
