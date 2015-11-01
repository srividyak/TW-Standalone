package com.tapwisdom.core.jobs.user;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.EsConfigSettingsDao;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.service.IUserTimeLineService;
import com.tapwisdom.core.jobs.BaseTWJob;
import com.tapwisdom.core.jobs.TWScheduledJob;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@TWScheduledJob(name = "user_timeline", parameterClasses = {
        EsConfigSettingsDao.class,
        UserDao.class,
        IUserTimeLineService.class
})
public class TimeLineJob extends BaseTWJob {

    private UserDao userDao;
    private IUserTimeLineService userTimeLineService;
    
    private static final Logger LOG = Logger.getLogger(TimeLineJob.class);
    private static final Logger METRICS_LOG = Logger.getLogger("metricsLog");
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static int USER_TIMELINE_MAX_THREADS = Integer.parseInt(reader.getProperty(Constants.USER_TIMELINE_MAX_THREADS, "4"));
    private ExecutorService service;
    private List<Callable<Void>> callables;

    public TimeLineJob(Integer jobFrequency, boolean isEnabled, EsConfigSettingsDao configSettingsDao, 
                       UserDao userDao, IUserTimeLineService userTimeLineService) {
        super(jobFrequency, configSettingsDao);
        this.userDao = userDao;
        this.userTimeLineService = userTimeLineService;
        this.isEnabled = isEnabled;
        service = Executors.newFixedThreadPool(USER_TIMELINE_MAX_THREADS);
    }

    @Override
    protected void startProcessing() {
        LOG.debug("Starting timeline job for user");
        int page = 0;
        List<User> users = userDao.getUsers(page);
        while (users != null && users.size() > 0) {
            for (User user : users) {
                initCallables(user);
                try {
                    List<Future<Void>> futures = service.invokeAll(callables);
                    // blocking responses till all data returned for this user
                    Long curTime = System.currentTimeMillis();
                    for (Future future : futures) {
                        try {
                            future.get();
                        } catch (ExecutionException e) {
                            LOG.error("FETCHING_FUTURE_FAILED_USER_TIMELINE:" + e.getMessage(), e);
                        }
                    }
                    int diff = (int) (System.currentTimeMillis() - curTime);
                    METRICS_LOG.info("Timeline Job for user: " + user.getId() + ", " + user.getEmail() + " => " + diff);
                } catch (InterruptedException e) {
                    LOG.error("CALLABLE_FAILED_USER_TIMELINE:" + e.getMessage(), e);
                }
            }
            users = userDao.getUsers(++page);
        }
    }
    
    private void initCallables(User user) {
        callables = new ArrayList<Callable<Void>>();
        QuestionsCallableUser questionsCallable = new QuestionsCallableUser(user, userTimeLineService);
        NewsCallableUser newsCallable = new NewsCallableUser(user, userTimeLineService);
        RecentUsersCallableUser recentUsersCallableUser = new RecentUsersCallableUser(user, userTimeLineService);
        callables.add(newsCallable);
        callables.add(recentUsersCallableUser);
        callables.add(questionsCallable);
    }
}
