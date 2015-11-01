package com.tapwisdom.core.daos.apis.impl;

import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.ConversationDao;
import com.tapwisdom.core.daos.documents.Conversation;
import com.tapwisdom.core.daos.documents.Message;
import org.apache.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by srividyak on 26/04/15.
 */
@Component
public class ConversationDaoImpl extends BaseDaoImpl<Conversation> implements ConversationDao {
    
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final Logger LOG = Logger.getLogger(ConversationDaoImpl.class);

    @Override
    public List<Conversation> getConversation(String conversationId, int page) throws TapWisdomException {
        Query query = new Query();
        query.addCriteria(Criteria.where("conversationId").is(conversationId));
        int maxNumMessages = Integer.parseInt(reader.getProperty(Constants.MAX_NUM_MESSAGES_IN_PAGE, "10"));
        query.with(new PageRequest(page, maxNumMessages));
        query.with(new Sort(Sort.Direction.DESC, "updatedAt"));
        List<Conversation> conversations =  operations.find(query, Conversation.class);
        return conversations;
    }

    public boolean updateConversation(Conversation conversation) throws TapWisdomException {
        Query query =  new Query();
        Update update = new Update();
        if (conversation.getConversationId() != null) {
            query.addCriteria(Criteria.where("conversationId").is(conversation.getConversationId()));
            if (conversation.getMessage() != null) {
                Message message = conversation.getMessage();
                if (message.getId() != null) {
                    query.addCriteria(Criteria.where("message._id").is(message.getId()));
                } else {
                    throw new TapWisdomException(1, "message cannot be updated without message id of a conversation");
                }
                update.set("message.messageText", message.getMessageText());
                if (message.getStatus() != null) {
                    update.set("message.status", message.getStatus());
                }
                WriteResult result = operations.updateFirst(query, update, Conversation.class);
                return result.isUpdateOfExisting();
            } else {
                throw new TapWisdomException(1, "Nothing to update");
            }
        } else {
            throw new TapWisdomException(1, "conversationId mandatory for updating conversation");
        }
    }
}
