package com.tapwisdom.core.jobs.user;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.EntityViewable;
import com.tapwisdom.core.daos.documents.QnAEntity;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserTimeLineEntityType;
import com.tapwisdom.core.daos.service.IUserTimeLineService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import java.util.List;

public class QuestionsCallableUser extends UserTimeLineCallable {
    
    private static final Logger LOG = Logger.getLogger(QuestionsCallableUser.class);

    public QuestionsCallableUser(User user, IUserTimeLineService timeLineService) {
        super(user, timeLineService);
    }

    @Override
    protected void process() throws TapWisdomException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("fetching questions for user: " + user.getId());
        }
        timeLineService.deleteQuestions(user.getId());
        List<EntityViewable<QnAEntity>> qnAEntityViewableList = timeLineService.getQuestionsOnCompaniesViewable(user.getId());
        if (!CollectionUtils.isEmpty(qnAEntityViewableList)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("saving questions for user: " + user.getId());
            }
            timeLineService.saveTimeLineEntities(qnAEntityViewableList, user.getId(), UserTimeLineEntityType.QUESTIONS);
        }
    }
}
