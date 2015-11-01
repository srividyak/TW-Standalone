package com.tapwisdom.core.misc.advisorpush;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.misc.ITriggerTranslateApp;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:miscJobs_test.xml"
})
public class TriggerAdvisorTranslateAppTest {
    
    @Autowired
    @Qualifier("advisorDataTranslateAndPush")
    private ITriggerTranslateApp app;
    
    @Autowired
    private MongoOperations operations;

    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final boolean isUserDataPushEnabled = Boolean.parseBoolean(reader.getProperty(Constants.ADVISOR_PUSH_ENABLED, "false"));
    
    @Test
    public void testTranslateAndPush() {
        if (isUserDataPushEnabled) {
            app.translateAndPush();
            List<User> users = operations.findAll(User.class);
            assert users != null;
            assert users.size() > 0;    
        } else {
            assert true;
        }
    }
    
    @After
    public void delete() {
        operations.findAllAndRemove(new Query(), User.class);
    }
    
}
