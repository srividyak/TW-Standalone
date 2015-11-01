package com.tapwisdom.core.misc.advisorpush;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserSource;
import com.tapwisdom.core.misc.DataTranslator;
import com.tapwisdom.core.misc.TriggerTranslateApp;
import com.tapwisdom.service.api.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(value = "advisorDataTranslateAndPush")
public class TriggerAdvisorTranslateApp extends TriggerTranslateApp<AdvisorData, User> {
    
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static boolean isAdvisorPushEnabled = Boolean.parseBoolean(reader.getProperty(Constants.ADVISOR_PUSH_ENABLED, "true"));
    
    @Autowired
    @Qualifier("advisorDataTranslator")
    private DataTranslator dataTranslator;

    @Autowired
    private IUserService userService;
    
    @Override
    public void push(User user) throws TapWisdomException {
        user.setSource(UserSource.advisor_form);
        userService.createUser(user);
    }

    @Override
    public DataTranslator<AdvisorData, User> getDataTranslator() {
        return dataTranslator;
    }

    @Override
    public boolean isEnabled() {
        return isAdvisorPushEnabled;
    }
}
