package com.tapwisdom.core.misc.companypush;

import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.Location;
import com.tapwisdom.core.misc.DataTranslator;
import org.springframework.stereotype.Component;

@Component(value = "companyDataTranslator")
public class CompanyDataTranslator implements DataTranslator<CompanyData, Company> {
    
    @Override
    public Company translate(CompanyData companyData) {
        Company company = new Company();
        company.setName(companyData.getName().toLowerCase().trim());
        company.setIndustry(companyData.getIndustry());
        String location = companyData.getLocation();
        String[] locationParts = location.split(",");
        Location[] locations = new Location[locationParts.length];
        int index = 0;
        for (String name : locationParts) {
            Location l = new Location();
            l.setName(name.toLowerCase().trim());
            locations[index++] = l;
        }
        company.setLocations(locations);
        company.setLogoUrl(companyData.getLogo());
        return company;
    }
}
