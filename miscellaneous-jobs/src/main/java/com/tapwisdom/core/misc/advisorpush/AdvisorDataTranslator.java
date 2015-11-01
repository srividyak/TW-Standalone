package com.tapwisdom.core.misc.advisorpush;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.core.misc.DataTranslator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component(value = "advisorDataTranslator")
public class AdvisorDataTranslator implements DataTranslator<AdvisorData, User> {
    private static final Logger LOG = Logger.getLogger(AdvisorDataTranslator.class);
    
    @Autowired
    private CompanyDao companyDao;
    
    @Override
    public User translate(AdvisorData advisor) {
        User user = new User();
        LiProfile liProfile = new LiProfile();
        user.setLinkedInProfile(liProfile);
        liProfile.setEmailAddress(advisor.getEmail());
        liProfile.setFirstName(advisor.getName());
        liProfile.setHeadLine(advisor.getHeadline());
        Location location = new Location();
        location.setName(advisor.getLocation());
        liProfile.setLocation(location);
        liProfile.setPictureUrl(advisor.getLinkedInPictureUrl());
        Position position = new Position();
        Company company = new Company();
        company.setName(advisor.getCompany());
        try {
            company = companyDao.getCompanyByName(advisor.getCompany().toLowerCase());
        } catch (TapWisdomException e) {
            LOG.error("Error occurred while fetching company", e);
        }
        position.setCompany(company);
        position.setIsCurrent(true);
        position.setTitle(advisor.getHeadline());
        liProfile.setPositions(Arrays.asList(new Position[]{position}));
        liProfile.setPublicProfileUrl(advisor.getLinkedInPublicProfileUrl());
        liProfile.setSummary(advisor.getSummary());
        return user;
    }
}
