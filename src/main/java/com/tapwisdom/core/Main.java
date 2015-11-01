package com.tapwisdom.core;

import com.tapwisdom.core.jobs.ProcessJobs;
import com.tapwisdom.core.misc.TriggerMiscApp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        TriggerMiscApp miscApp = context.getBean(TriggerMiscApp.class);
        miscApp.init();
        ProcessJobs jobs = new ProcessJobs(context);
    }
}
