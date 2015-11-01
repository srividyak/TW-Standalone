package com.tapwisdom.core.misc;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TriggerMiscApp {
    
    private static final Logger LOG = Logger.getLogger(TriggerMiscApp.class);
    
    @Autowired
    private List<ITriggerTranslateApp> translateApps;
    
    public void init() {
        if (translateApps != null) {
            for (ITriggerTranslateApp translateApp : translateApps) {
                translateApp.translateAndPush();
            }
        } else {
            LOG.info("No misc apps present to enable translation");
        }
    }
    
}
