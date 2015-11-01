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

public class NewsCallableUser extends UserTimeLineCallable {

    private static final Logger LOG = Logger.getLogger(NewsCallableUser.class);

    public NewsCallableUser(User user, IUserTimeLineService timeLineService) {
        super(user, timeLineService);
    }

    @Override
    protected void process() throws TapWisdomException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("fetching news for user: " + user.getId());
        }
        timeLineService.deleteNews(user.getId());
        List<EntityViewable<News>> newsEntityViewableList = timeLineService.getNews(user.getId());
        if (!CollectionUtils.isEmpty(newsEntityViewableList)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("saving news for user: " + user.getId());
            }
            timeLineService.saveTimeLineEntities(newsEntityViewableList, user.getId(), UserTimeLineEntityType.NEWS);
        }
    }
}
