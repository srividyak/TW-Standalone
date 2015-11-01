package com.tapwisdom.core.jobs.user;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.EntityViewable;
import com.tapwisdom.core.daos.documents.News;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserTimeLineEntityType;
import com.tapwisdom.core.daos.service.IUserTimeLineService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class RecentUsersCallableUser extends UserTimeLineCallable {
    private static final Logger LOG = Logger.getLogger(NewsCallableUser.class);
    
    public RecentUsersCallableUser(User user, IUserTimeLineService timeLineService) {
        super(user, timeLineService);
    }

    @Override
    protected void process() throws TapWisdomException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("fetching recent users for user: " + user.getId());
        }
        timeLineService.deleteUsers(user.getId());
        List<EntityViewable<User>> usersEntityViewableList = timeLineService.getRecentUsersFromWatchedCompanies(user.getId());
        if (!CollectionUtils.isEmpty(usersEntityViewableList)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("saving users for user: " + user.getId());
            }
            timeLineService.saveTimeLineEntities(usersEntityViewableList, user.getId(), UserTimeLineEntityType.USERS);
        }
    }
}
