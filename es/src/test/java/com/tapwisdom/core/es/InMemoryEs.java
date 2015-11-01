package com.tapwisdom.core.es;

import com.github.tlrx.elasticsearch.test.EsSetup;

import static com.github.tlrx.elasticsearch.test.EsSetup.*;

/**
 * Created by srividyak on 12/07/15.
 */
public class InMemoryEs {
    
    EsSetup esSetup;
    
    public InMemoryEs() {
        esSetup = new EsSetup();
        esSetup.execute(deleteAll(), createIndex("tw_test"));
    }
    
    public void destroy() {
        esSetup.terminate();
    }
    
}
