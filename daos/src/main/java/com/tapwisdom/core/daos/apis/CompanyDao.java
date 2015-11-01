package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Company;

import java.util.List;

public interface CompanyDao extends BaseDao<Company> {
    
    public List<Company> getCompaniesByLocation(String location , int page) throws TapWisdomException;

    public List<Company> getCompaniesByIndustry(String industry, int page) throws TapWisdomException;

    public List<Company> getAllCompanies() throws TapWisdomException;
    
    public List<Company> getAllCompanies(int page) throws TapWisdomException;

    public Boolean updateCompany(Company company) throws TapWisdomException;

    public Company getCompanyByName(String name) throws TapWisdomException;
    
}
