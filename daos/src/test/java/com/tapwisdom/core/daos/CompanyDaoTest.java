package com.tapwisdom.core.daos;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class CompanyDaoTest {
    
    @Autowired
    private CompanyDao companyDao;
    
    private Company company;

    @Autowired
    private MongoOperations operations;
    
    @Before
    public void createCompany() {
        company = new Company();
        String companyLocation = "Bangalore";
        Location location = new Location();
        location.setName(companyLocation);
        company.setIndustry("software");
        company.setName("tapwisdom");
        company.setWebsite("http://tapwisdom.com");
        company.setNumEmployees(4L);
        company.setLocations(new Location[]{location});
        try {
            companyDao.save(company);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testCompaniesByLocation() {
        try {
            List<Company> companies = companyDao.getCompaniesByLocation("Bangalore", 0);
            assert companies != null;
            assert companies.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testCompaniesByIndustry() {
        try {
            List<Company> companies = companyDao.getCompaniesByIndustry("software", 0);
            assert companies != null;
            assert companies.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testCompaniesByName() {
        try {
            Company company = companyDao.getCompanyByName("tapwisdom");
            assert company != null;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    public void testAllCompanies() {
        try {
            List<Company> companies = companyDao.getAllCompanies();
            assert companies != null;
            assert companies.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void deleteCompany() {
        Query query = new Query();
        operations.findAllAndRemove(query, Company.class);
    }
    
}
