package com.tapwisdom.core.misc;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.misc.advisorpush.AdvisorData;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CSVDataReaderImpl<O> implements IDataReader<O> {
    
    private PropertyReader reader = PropertyReader.getInstance();
    private final static Logger LOG = Logger.getLogger(CSVDataReaderImpl.class);
    
    protected ValueProcessorProvider valueProcessorProvider;
    protected CSVReaderBuilder builder;
    protected List<O> dataList = new ArrayList();

    protected abstract void setData();
    
    protected abstract String getFilePath();

    @Override
    public void loadData() {
        String filePath = getFilePath();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            valueProcessorProvider = new ValueProcessorProvider();
            builder = new CSVReaderBuilder<O>(bufferedReader);
            // strategy needs to be set to ensure it takes , (read as comma) as the separator. 
            // Default is taken as ; (semicolon)
            builder.strategy(CSVStrategy.UK_DEFAULT);
            setData();
        } catch (FileNotFoundException e) {
            LOG.error("Could not find file: " + filePath, e);
        }
    }

    @Override
    public List<O> getDataList() {
        return dataList;
    }
}
