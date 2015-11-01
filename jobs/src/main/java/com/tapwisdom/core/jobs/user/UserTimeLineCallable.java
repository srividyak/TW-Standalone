package com.tapwisdom.core.jobs.user;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.service.IUserTimeLineService;

import java.util.concurrent.Callable;

public abstract class UserTimeLineCallable implements Callable {

    protected final User user;
    protected final IUserTimeLineService timeLineService;

    public UserTimeLineCallable(User user, IUserTimeLineService timeLineService) {
        this.user = user;
        this.timeLineService = timeLineService;
    }
    
    protected abstract void process() throws TapWisdomException;

    @Override
    public Void call() throws Exception {
        process();
        return null;
    }
}
