package com.tapwisdom.core.misc;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class TriggerTranslateApp<O, T> implements ITriggerTranslateApp {
    
    private static final Logger LOG = Logger.getLogger(TriggerTranslateApp.class);
    
    public abstract void push(T object) throws TapWisdomException;
    
    public abstract DataTranslator<O, T> getDataTranslator();
    
    public abstract boolean isEnabled();
    
    @Autowired
    private IDataReader<O> dataReader;
    
    @Override
    public final void translateAndPush() {
        if (isEnabled()) {
            List<O> list = getOriginalData();
            DataTranslator<O, T> dataTranslator = getDataTranslator();
            for (O o : list) {
                T translatedData = dataTranslator.translate(o);
                try {
                    push(translatedData);
                } catch (TapWisdomException e) {
                    LOG.error("Error while pushing data to data store: " + e.getMessage(), e);
                }
            }
        }
    }
    
    public List<O> getOriginalData() {
        dataReader.loadData();
        return dataReader.getDataList();
    }
}
