package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CreditMgtDao;
import com.tapwisdom.core.daos.documents.CreditMgt;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * Created by sachin on 23/8/15.
 */

@Component
public class CreditMgtDaoImpl implements CreditMgtDao {

    private static final Logger LOG = Logger.getLogger(CreditMgtDaoImpl.class);

    @Autowired
    protected MongoOperations operations;

    @Override
    public int getCredits(String userId) throws TapWisdomException {
        Query query = new Query(Criteria.where("userId").is(userId));
        CreditMgt creditMgt = operations.findOne(query, CreditMgt.class);
        if(creditMgt !=null)
            return creditMgt.getCredits();
        return 0;
    }

    @Override
    public void addCredits(String userId, int credits) throws TapWisdomException {
        Query query = new Query(Criteria.where("userId").is(userId));
        Update update =  new Update();
        update.set("userId",userId);
        update.inc("credits", credits);
        operations.upsert(query,update,CreditMgt.class);
    }

    @Override
    public void redeemCredits(String userId, int credits) throws TapWisdomException {
        if(credits<=300 && (credits%50==0)){
            Query query = new Query(Criteria.where("userId").is(userId));
            int currentCredits = 0;
            CreditMgt creditMgt = operations.findOne(query, CreditMgt.class);
            if(creditMgt !=null)
                currentCredits=creditMgt.getCredits();
            else
                throw new TapWisdomException(1,"credits redemption request failed for credits:"+ credits);
            if(currentCredits>=credits) {
                Update update = new Update();
                update.set("userId", userId);
                update.set("latsRedemptionRequest", credits);
                update.inc("credits", -credits);
                operations.upsert(query, update, CreditMgt.class);
                //TODO have to inform admin about request

                /////////////////
            }
            else
                throw new TapWisdomException(1,"credits redemption request failed for credits:"+ credits);
        }else{
            throw new TapWisdomException(1,"credits redemption request failed for credits:"+ credits);
        }
    }
}
