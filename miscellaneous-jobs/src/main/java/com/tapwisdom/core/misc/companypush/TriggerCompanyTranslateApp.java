package com.tapwisdom.core.misc.companypush;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.misc.CSVDataReaderImpl;
import com.tapwisdom.core.misc.DataTranslator;
import com.tapwisdom.core.misc.IDataReader;
import com.tapwisdom.core.misc.TriggerTranslateApp;
import com.tapwisdom.service.api.ICompanyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component(value = "companyDataTranslateAndPush")
public class TriggerCompanyTranslateApp extends TriggerTranslateApp<CompanyData, Company> {

    private static final PropertyReader reader = PropertyReader.getInstance();
    private static boolean isCompanyDataPushEnabled = Boolean.parseBoolean(reader.getProperty(Constants.COMPANY_DATA_PUSH_ENABLED, "true"));
    
    @Autowired
    @Qualifier("companyDataTranslator")
    private DataTranslator dataTranslator;

    @Autowired
    private ICompanyService companyService;

    @Override
    public void push(Company company) throws TapWisdomException {
        companyService.createCompanies(Arrays.asList(company));
    }

    @Override
    public DataTranslator<CompanyData, Company> getDataTranslator() {
        return dataTranslator;
    }

    @Override
    public boolean isEnabled() {
        return isCompanyDataPushEnabled;
    }
}