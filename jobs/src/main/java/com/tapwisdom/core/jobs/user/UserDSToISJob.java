package com.tapwisdom.core.jobs.user;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.EsConfigSettingsDao;
import com.tapwisdom.core.daos.apis.UserCompanyConnectionDao;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.core.es.UserBuilder;
import com.tapwisdom.core.es.repositories.IUserSearchRepository;
import com.tapwisdom.core.es.repositories.UserRepository;
import com.tapwisdom.core.jobs.BaseTWJob;
import com.tapwisdom.core.jobs.TWScheduledJob;
import org.apache.log4j.Logger;

import java.util.*;

@TWScheduledJob(name = "user_to_index", parameterClasses = {
        EsConfigSettingsDao.class,
        UserDao.class,
        UserCompanyConnectionDao.class,
        UserRepository.class,
        IUserSearchRepository.class,
        CompanyDao.class
})
public class UserDSToISJob extends BaseTWJob {

    private UserDao userDao;

    private UserCompanyConnectionDao userCompanyConnectionDao;

    private UserRepository repository;

    private IUserSearchRepository userSearchRepository;

    private Long timestampToBeSaved;

    private CompanyDao companyDao;

    private Map<String, Company> companyMap = new HashMap<String, Company>();

    private static final Logger LOG = Logger.getLogger(UserDSToISJob.class);
    private static final Logger METRICS_LOG = Logger.getLogger("metricsLog");

    public UserDSToISJob(Integer jobFrequency, boolean isEnabled, EsConfigSettingsDao configSettingsDao,
                         UserDao userDao, UserCompanyConnectionDao userCompanyConnectionDao,
                         UserRepository userRepository, IUserSearchRepository userSearchRepository,
                         CompanyDao companyDao) {
        super(jobFrequency, configSettingsDao);
        this.userDao = userDao;
        this.isEnabled = isEnabled;
        this.userCompanyConnectionDao = userCompanyConnectionDao;
        this.repository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.companyDao = companyDao;
    }

    @Override
    protected void startProcessing() throws TapWisdomException {
        EsConfigSettings configSettings = null;
        try {
            configSettings = getConfigSettings();
            Long timestampAfter = configSettings != null ? configSettings.getLastUserToIndexUpdateTime() : null;
            Long timestampBefore = new Date().getTime();
            timestampToBeSaved = timestampBefore;
            List<User> users = userDao.getUsersUpdatedWithinTimeRange(timestampAfter, timestampBefore);
            if (LOG.isDebugEnabled()) {
                LOG.debug("num users to be updated: " + users.size() + " between " + timestampAfter +
                        " and " + timestampBefore);
            }
            for (User user : users) {
                Long curTime = System.currentTimeMillis();
                UserView userView = (UserView) user;
                if (userView.getLinkedInProfile() != null) {
                    LiProfile liProfile = userView.getLinkedInProfile();
                    if (liProfile.getPositions() != null) {
                        List<Position> positions = liProfile.getPositions();
                        for (Position position : positions) {
                            Company company = position.getCompany();
                            if (company != null) {
                                if (company.getIndustry() == null) {
                                    if (companyMap.containsKey(company.getName())) {
                                        company.setIndustry(companyMap.get(company.getName()).getIndustry());
                                    }
                                }
                            }
                        }
                    }
                }
                com.tapwisdom.core.es.documents.User esUser = UserBuilder.buildUser(userView);

                User updatedUser = User.buildEmptyUser();
                updatedUser.setId(user.getId());
                updatedUser.setLastWrittenToIndexedStore(new Date().getTime());

                com.tapwisdom.core.es.documents.User existingUser = userSearchRepository.getUserByUserId(user.getId());
                if (existingUser == null) {
                    repository.save(esUser);
                } else {
                    existingUser.setUserView(userView);
                    repository.save(existingUser);
                }
                userDao.updateUser(updatedUser);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("successfully exported user: " + userView.getId() + " with email: " + userView.getEmail()
                            + " to es");
                }
                int diff = (int) (System.currentTimeMillis() - curTime);
                METRICS_LOG.info("UserDAToISJob for: " + user.getId() + ":" + user.getEmail() + " => " + diff);
            }
        } catch (TapWisdomException e) {
            LOG.error("Error encountered while processing user to es move job: " + e.getMessage(), e);
            throw new TapWisdomException(e.getMessage(), e);
        } catch (Exception e) {
            LOG.error("Error encountered while processing user to es move job: " + e.getMessage(), e);
            throw new TapWisdomException(e.getMessage(), e);
        }
    }

    @Override
    public void saveStatus() {
        try {
            EsConfigSettings configSettings = getConfigSettings();
            if (configSettings == null) {
                configSettings = new EsConfigSettings();
            }
            configSettings.setLastUserToIndexUpdateTime(timestampToBeSaved);
            setConfigSettings(configSettings);
            saveConfigSettings();
        } catch (TapWisdomException e) {
            LOG.error("Unable to fetch config settings: " + e.getMessage(), e);
        }
    }

    @Override
    public void initialize() {
        try {
            List<Company> companies = companyDao.getAllCompanies();
            if (LOG.isDebugEnabled()) {
                LOG.debug("initializing export of user to elasticsearch index");
                LOG.debug("Found " + companies.size() + " companies");
            }
            for (Company company : companies) {
                // even if there are duplicates and map gets overwritten, it does not matter since we are mainly concerned about industry
                companyMap.put(company.getName(), company);
            }
        } catch (TapWisdomException e) {
            LOG.error("Unable to initialize companies: " + e.getMessage(), e);
        }
    }

}
