package com.tapwisdom.core.jobs;

import akka.actor.UntypedActor;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EsConfigSettingsDao;
import com.tapwisdom.core.daos.documents.EsConfigSettings;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by srividyak on 12/07/15.
 */
public abstract class BaseTWJob extends UntypedActor implements TWJob {
    
    private EsConfigSettingsDao configSettingsDao;
    
    private EsConfigSettings configSettings;
    
    protected boolean isEnabled;
    
    private static final Logger LOG = Logger.getLogger(BaseTWJob.class);
    private static final Logger METRICS_LOG = Logger.getLogger("metricsLog");
    
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    
    private Integer jobFrequency;
    
    protected void startProcessing() throws TapWisdomException {
        
    }

    public BaseTWJob(Integer jobFrequency, EsConfigSettingsDao configSettingsDao) {
        this.jobFrequency = jobFrequency;
        this.configSettingsDao = configSettingsDao;
    }

    protected EsConfigSettings getConfigSettings() throws TapWisdomException {
        if (configSettings == null) {
            setConfigSettings();
        }
        return configSettings;
    }

    private void setConfigSettings() throws TapWisdomException {
        this.configSettings = configSettingsDao.getSettings();
    }
    
    protected void setConfigSettings(EsConfigSettings settings) {
        this.configSettings = settings;
    }

    @Override
    public final void process() {
        if (isJobEnabled()) {
            LOG.debug("Job: " + BaseTWJob.this.getClass().getName() + " is enabled");
            if (jobFrequency <= 0) {
                new BaseTWJobInternal().run();
            } else {
                executorService.scheduleAtFixedRate(new BaseTWJobInternal(), 0, jobFrequency, TimeUnit.MINUTES);
            }    
        } else {
            LOG.debug("Job: " + BaseTWJob.this.getClass().getName() + " is disabled");
        }
    }
    
    protected void saveConfigSettings() {
        try {
            configSettingsDao.save(configSettings);
        } catch (TapWisdomException e) {
            LOG.error("Error saving config settings: " + e.getMessage(), e);
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void reinitialize() {
        if (isJobEnabled()) {
            shutdown();
            initialize();
            process();
        }
    }

    public boolean isJobEnabled() {
        return isEnabled;
    }

    @Override
    public void onReceive(Object o)  {
        try {
            if (o instanceof JobCommand) {
                if (o == JobCommand.START) {
                    process();
                } else if (o == JobCommand.RESTART) {
                    reinitialize();
                } else if (o == JobCommand.SHUTDOWN) {
                    shutdown();
                } else {
                    LOG.error("Invalid command passed: " + o);
                }
            } else {
                LOG.error("Invalid type of command passed: " + o.getClass());
            }
        } catch (Exception e) {
            LOG.error("Error while processing the command: " + e.getMessage(), e);
        }
    }
    
    public void initialize() {
        
    }
    
    @Override
    public void saveStatus() {
        
    }
    
    private class BaseTWJobInternal implements Runnable {

        @Override
        public void run() {
            try {
                LOG.info("Running Job " + BaseTWJob.this.getClass().getName());
                Long curTime = System.currentTimeMillis();
                setConfigSettings();
                initialize();
                startProcessing();
                saveStatus();
                int diff = (int) (System.currentTimeMillis() - curTime);
                METRICS_LOG.info("JOB_TIME: " + BaseTWJob.this.getClass().getSimpleName() + " => " + diff);
            } catch (TapWisdomException e) {
                LOG.error(e.getMessage(), e);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

}
