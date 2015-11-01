package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Call;

import java.util.List;

/**
 * Created by srividyak on 03/05/15.
 */
public interface CallDao extends BaseDao<Call> {
    
    public Call getCall(String callId) throws TapWisdomException;
    
    public List<Call> getCalls(List<String> callIds) throws TapWisdomException;
    
    public List<Call> getCalls(String giverId, String seekerId) throws TapWisdomException;
    
    public List<Call> getCalls(String giverId, String seekerId, Long startTime) throws TapWisdomException;

    /**
     * * Gets all calls of userId where userId is both a giver and seeker
     * @param userId
     * @param startTime
     * @return
     * @throws TapWisdomException
     */
    public List<Call> getMyCalls(String userId, Long startTime) throws TapWisdomException;

    public List<Call> getMyCalls(String userId) throws TapWisdomException;
    
    public Boolean updateCall(Call call) throws TapWisdomException;
    
}
