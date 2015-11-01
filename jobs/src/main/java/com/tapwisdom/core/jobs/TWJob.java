package com.tapwisdom.core.jobs;

import akka.actor.Actor;

/**
 * Created by srividyak on 12/07/15.
 */
public interface TWJob extends Actor {

    public void process();

    public void shutdown();
    
    public void reinitialize();
    
    public boolean isJobEnabled();
    
    public void saveStatus();
    
    public void initialize();
    
}
