package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.documents.Company;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyDaoImpl extends BaseDaoImpl<Company> implements CompanyDao {

    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final int maxNumResultsInPage = Integer.parseInt(reader.getProperty(Constants.MAX_NUM_MESSAGES_IN_PAGE, "10"));
    private static final Logger LOG = org.apache.log4j.Logger.getLogger(CompanyDaoImpl.class);

    @Override
    public Boolean updateCompany(Company company) throws TapWisdomException {
        if (company.getId() != null) {
            String companyId = company.getId();
            LOG.debug("updating user with id: " + companyId);
            Update update = new Update();

            if (company.getNumQuestionsAnswered() != 0) {
                update.inc("numQuestionsAnswered", 1);
            }
            if (company.getNumQuestionsAsked() != 0) {
                update.inc("numQuestionsAsked", 1);
            }
            if (company.getName() != null) {
                update.set("name", company.getName());
            }
            if (company.getIndustry() != null) {
                update.set("industry", company.getIndustry());
            }
            if (company.getLocations() != null) {
                update.set("location", company.getLocations());
            }
            // Not sure about this : Increment or update the value
            if (company.getNumEmployees() != null) {
                update.set("numEmployees", company.getNumEmployees());
            }
            if (company.getWebsite() != null) {
                update.set("website", company.getWebsite());
            }
            if (company.getLogoUrl() != null) {
                update.set("logoUrl", company.getLogoUrl());
            }
            if (company.getUpdatedAt() != null) {
                update.set("updatedAt", company.getUpdatedAt());
            }

            try {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(companyId)));
                return operations.updateFirst(query, update, Company.class).isUpdateOfExisting();
            } catch (Exception e) {
                LOG.error("Could not update a field: " + e.getMessage(), e);
                throw new TapWisdomException(1, "Company update was not successful!!");
            }
        }

        return false;
    }

    @Override
    public List<Company> getCompaniesByLocation(String location, int page) throws TapWisdomException {
        Query query = new Query(Criteria.where("locations").elemMatch(Criteria.where("name").is(location)));
        query.with(new PageRequest(page, maxNumResultsInPage));
        return operations.find(query, Company.class);
    }

    @Override
    public List<Company> getCompaniesByIndustry(String industry, int page) throws TapWisdomException {
        Query query = new Query(Criteria.where("industry").is(industry));
        query.with(new PageRequest(page, maxNumResultsInPage));
        return operations.find(query, Company.class);
    }

    @Override
    public List<Company> getAllCompanies() throws TapWisdomException {
        Query query = new Query();
        return operations.find(query, Company.class);
    }

    @Override
    public List<Company> getAllCompanies(int page) throws TapWisdomException {
        Query query = new Query();
        query.with(new PageRequest(page, maxNumResultsInPage));
        return operations.find(query, Company.class);
    }

    @Override
    public Company getCompanyByName(String name) throws TapWisdomException {
        Query query = new Query(Criteria.where("name").is(name));
        return operations.findOne(query, Company.class);
    }

}
