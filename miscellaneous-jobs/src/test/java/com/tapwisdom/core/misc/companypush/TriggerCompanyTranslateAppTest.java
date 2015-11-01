package com.tapwisdom.core.misc.companypush;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.misc.ITriggerTranslateApp;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:miscJobs_test.xml"
})
public class TriggerCompanyTranslateAppTest {

    @Autowired
    @Qualifier("companyDataTranslateAndPush")
    private ITriggerTranslateApp app;

    @Autowired
    private MongoOperations operations;

    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final boolean isCompanyReadCsvEnabled = Boolean.parseBoolean(reader.getProperty(Constants.COMPANY_DATA_PUSH_ENABLED, "false"));

    @Test
    public void testTranslateAndPush() {
        if (isCompanyReadCsvEnabled) {
            app.translateAndPush();
            List<Company> companies = operations.findAll(Company.class);
            assert companies != null;
            assert companies.size() > 0;    
        } else {
            assert true;
        }
    }

    @After
    public void delete() {
        operations.findAllAndRemove(new Query(), Company.class);
    }
}
