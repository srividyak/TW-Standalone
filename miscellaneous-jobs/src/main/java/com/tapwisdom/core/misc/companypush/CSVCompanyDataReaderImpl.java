package com.tapwisdom.core.misc.companypush;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.misc.CSVDataReaderImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CSVCompanyDataReaderImpl extends CSVDataReaderImpl<CompanyData> {

    private PropertyReader reader = PropertyReader.getInstance();
    private final String filePath = reader.getProperty(Constants.CSV_COMPANY_DATA_FILE_PATH, "");
    private final static Logger LOG = Logger.getLogger(CSVCompanyDataReaderImpl.class);

    @Override
    protected void setData() {
        builder.strategy(CSVStrategy.UK_DEFAULT);
        CSVReader<CompanyData> companyDataCSVReader = builder.entryParser(
                new AnnotationEntryParser<CompanyData>(CompanyData.class,
                        valueProcessorProvider)).build();
        try {
            dataList = companyDataCSVReader.readAll();
        } catch (IOException e) {
            LOG.error("Error reading csv file for advisor details", e);
        }
    }

    @Override
    protected String getFilePath() {
        return filePath;
    }
}
