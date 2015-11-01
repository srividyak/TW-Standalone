package com.tapwisdom.core.jobs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.common.util.Utils;
import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;

import java.util.*;

public class ProcessJobs {
    
    private String rootPackageName;
    
    private ActorSystem actorSystem;
    
    private ApplicationContext context;
    
    private static final Logger LOG = Logger.getLogger(ProcessJobs.class);
    
    private static final PropertyReader reader = PropertyReader.getInstance();
    
    private static Map<String, String> jobFrequencyMap = new HashMap();
    private static Set<String> jobEnabledSet = new HashSet<String>();
    
    static {
        String jobFrequencyJson = reader.getProperty("jobRefreshTime", "{}");
        String enabledJobsAsString = reader.getProperty("jobActiveStatus", "");
        try {
            jobFrequencyMap = Utils.getObjectFromString(jobFrequencyJson, Map.class);
            String[] enabledJobs = enabledJobsAsString.split(",");
            for (String enabledJob : enabledJobs) {
                jobEnabledSet.add(enabledJob);
            }
        } catch (Exception e) {
            LOG.error("Error reading job frequency JSON");
        }
        
    }

    public ProcessJobs(ApplicationContext context) {
        this.context = context;
        try {
            rootPackageName = this.getClass().getPackage().getName();
            actorSystem = ActorSystem.create("jobs");
            initAllJobs();
        } catch (Exception e) {
            LOG.error("error starting jobs: " + e.getMessage(), e);
        } finally {
            actorSystem.shutdown();
        }
    }

    private void initAllJobs() {
        Reflections reflections = new Reflections(rootPackageName);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(TWScheduledJob.class);
        Iterator<Class<?>> iterator = annotatedClasses.iterator();
        if (LOG.isDebugEnabled()) {
            LOG.debug("initializing all jobs");
        }
        while (iterator.hasNext()) {
            Class<?> clazz = iterator.next();
            TWScheduledJob job = clazz.getAnnotation(TWScheduledJob.class);
            Class[] classes = job.parameterClasses();
            Object[] parameters = new Object[classes.length + 2];
            int index = 0;
            if (jobFrequencyMap.containsKey(job.name())) {
                parameters[index++] = Integer.parseInt(jobFrequencyMap.get(job.name()));
            } else {
                parameters[index++] = 0;
            }
            if (jobEnabledSet.contains(job.name())) {
                parameters[index++] = true;
            } else {
                parameters[index++] = false;
            }
            for (Class c : classes) {
                parameters[index++] = context.getBean(c);
            }
            try {
                ActorRef actorRef = actorSystem.actorOf(Props.create(clazz, parameters));
                actorRef.tell(JobCommand.START, ActorRef.noSender());
            } catch (Exception e) {
                LOG.error("Error while initializing actor: " + e.getMessage(), e);
            }
        }
    }
    
}
