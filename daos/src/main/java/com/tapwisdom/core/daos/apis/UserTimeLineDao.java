package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.daos.documents.UserTimeLine;
import com.tapwisdom.core.daos.documents.UserTimeLineEntityType;

import java.util.List;

public interface UserTimeLineDao extends BaseDao<UserTimeLine> {
    
    List<UserTimeLine> getUserTimeLine(String user, int page);
    
    void deleteEntities(UserTimeLineEntityType entityType, String userId);
    
}
