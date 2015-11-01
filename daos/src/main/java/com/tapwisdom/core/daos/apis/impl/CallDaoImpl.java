package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CallDao;
import com.tapwisdom.core.daos.documents.Call;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by srividyak on 03/05/15.
 */
@Component
public class CallDaoImpl extends BaseDaoImpl<Call> implements CallDao {
    
    @Override
    public Call getCall(String callId) throws TapWisdomException {
        List<String> confIds = new ArrayList<String>();
        confIds.add(callId);
        List<Call> calls = getCalls(confIds);
        if (calls.isEmpty()) {
            throw new TapWisdomException(1, "No call exists with callId : " + callId);
        } else {
            return calls.get(0);
        }
    }

    @Override
    public List<Call> getCalls(List<String> callIds) throws TapWisdomException {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(callIds));
        List<Call> calls = operations.find(query, Call.class);
        return calls;
    }

    @Override
    public List<Call> getCalls(String giverId, String seekerId) throws TapWisdomException {
        List<Call> calls = getCalls(giverId, seekerId, new Date().getTime());
        return calls;
    }

    @Override
    public List<Call> getCalls(String giverId, String seekerId, Long startTime) throws TapWisdomException {
        Query query = new Query();
        Criteria giverIdCriteria = Criteria.where("giverId").is(giverId);
        Criteria seekerIdCriteria = Criteria.where("seekerId").is(seekerId);
        Criteria startTimeCriteria = Criteria.where("startTime").gte(startTime);
        long nextMonthInMillis = 30 * 24 * 60 * 60 * 1000L + startTime;
        Criteria nextStartTimeCriteria = Criteria.where("startTime").lte(nextMonthInMillis);
        query.addCriteria(giverIdCriteria);
        query.addCriteria(seekerIdCriteria);
        query.addCriteria(startTimeCriteria.andOperator(nextStartTimeCriteria));
        List<Call> calls = operations.find(query, Call.class);
        return calls;
    }
    
    @Override
    public List<Call> getMyCalls(String userId) throws TapWisdomException {
        List<Call> calls = getMyCalls(userId, new Date().getTime());
        return calls;
    }

    @Override
    public List<Call> getMyCalls(String userId, Long startTime) throws TapWisdomException {
        Query query = new Query();
        Criteria giverIdCriteria = Criteria.where("giverId").is(userId);
        Criteria seekerIdCriteria = Criteria.where("seekerId").is(userId);
        Criteria startTimeCriteria = Criteria.where("startTime").gte(startTime);
        long nextMonthInMillis = 30 * 24 * 60 * 60 * 1000L;
        Criteria nextStartTimeCriteria = Criteria.where("startTime").lte(startTime + nextMonthInMillis);
        Criteria allCriteria = startTimeCriteria.andOperator(nextStartTimeCriteria).orOperator(giverIdCriteria, seekerIdCriteria);
        query.addCriteria(allCriteria);
        List<Call> calls = operations.find(query, Call.class);
        return calls;
    }

    /**
     * * Supports updating startTime, endTime, duration, ratings, callStatus
     * @param call
     * @return
     */
    @Override
    public Boolean updateCall(Call call) throws TapWisdomException{
        Update update = new Update();
        if (call.getId() != null) {
            if (call.getStartTime() != null) {
                update.set("startTime", call.getStartTime());
            }
            if (call.getEndTime() != null) {
                update.set("endTime", call.getEndTime());
            }
            if (call.getDuration() != null) {
                update.set("duration", call.getDuration());
            }
            if (call.getRatings() != null) {
                update.set("ratings", call.getRatings());
            }
            if (call.getStatus() != null) {
                update.set("status", call.getStatus());
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(call.getId()));
            return operations.updateFirst(query, update, Call.class).isUpdateOfExisting();
        } else {
            throw new TapWisdomException(1, "cannot update call without call id");
        }
    }
}
