package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.UserTimeLineDao;
import com.tapwisdom.core.daos.documents.UserTimeLine;
import com.tapwisdom.core.daos.documents.UserTimeLineEntityType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserTimeLineDaoImpl extends BaseDaoImpl<UserTimeLine> implements UserTimeLineDao {

    private static final PropertyReader reader = PropertyReader.getInstance();
    private static int USER_TIMELINE_MAX_RESULTS = Integer.parseInt(reader.getProperty(Constants.MAX_RES_IN_PAGE, "50"));

    @Override
    public List<UserTimeLine> getUserTimeLine(String user, int page) {
        Query query = new Query(Criteria.where("userId").is(user));
        Sort sort = new Sort(Sort.Direction.DESC, "entityTimestamp");
        query.with(new PageRequest(page, USER_TIMELINE_MAX_RESULTS, sort));
        List<UserTimeLine> userTimeLineList = operations.find(query, UserTimeLine.class);
        return userTimeLineList;
    }

    @Override
    public void deleteEntities(UserTimeLineEntityType entityType, String userId) {
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("userId").is(userId),
                Criteria.where("entityType").is(entityType)
        ));
        operations.findAllAndRemove(query, UserTimeLine.class);
    }
}
