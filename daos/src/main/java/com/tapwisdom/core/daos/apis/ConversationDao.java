package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Conversation;
import com.tapwisdom.core.daos.documents.Message;

import java.util.List;

/**
 * Created by srividyak on 20/04/15.
 */
public interface ConversationDao extends BaseDao<Conversation> {

    public List<Conversation> getConversation(String conversationId, int page) throws TapWisdomException;

    public boolean updateConversation(Conversation conversation) throws TapWisdomException;
}
