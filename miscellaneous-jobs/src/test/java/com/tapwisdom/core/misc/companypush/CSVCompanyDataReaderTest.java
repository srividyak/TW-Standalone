package com.tapwisdom.core.misc.companypush;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.misc.IDataReader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:miscJobs_test.xml"
})
public class CSVCompanyDataReaderTest {

    @Autowired
    private IDataReader<CompanyData> companyDataReader;
    
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final boolean isCompanyReadCsvEnabled = Boolean.parseBoolean(reader.getProperty(Constants.COMPANY_DATA_PUSH_ENABLED, "false"));

    @Test
    public void testReadData() {
        if (isCompanyReadCsvEnabled) {
            companyDataReader.loadData();
            List<CompanyData> companyDataList = companyDataReader.getDataList();
            Assert.assertNotEquals(companyDataList.size(), 0);    
        } else {
            assert true;
        }
    }
    
}
