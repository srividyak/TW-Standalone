package com.tapwisdom.core.misc.advisorpush;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.misc.CSVDataReaderImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVAdvisorDataReaderImpl extends CSVDataReaderImpl<AdvisorData> {
    
    private PropertyReader reader = PropertyReader.getInstance();
    private final String filePath = reader.getProperty(Constants.CSV_USER_DATA_FILE_PATH, "");
    private final static Logger LOG = Logger.getLogger(CSVAdvisorDataReaderImpl.class);

    @Override
    protected void setData() {
        builder.strategy(CSVStrategy.UK_DEFAULT);
        CSVReader<AdvisorData> advisorDataCSVReader = builder.entryParser(
                new AnnotationEntryParser<AdvisorData>(AdvisorData.class,
                        valueProcessorProvider)).build();
        try {
            dataList = advisorDataCSVReader.readAll();
        } catch (IOException e) {
            LOG.error("Error reading csv file for advisor details", e);
        }
    }

    @Override
    protected String getFilePath() {
        return filePath;
    }
}
