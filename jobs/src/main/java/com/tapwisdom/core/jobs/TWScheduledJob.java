package com.tapwisdom.core.jobs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by srividyak on 12/07/15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TWScheduledJob {
    
    String name();
    
    Class[] parameterClasses();
    
}
