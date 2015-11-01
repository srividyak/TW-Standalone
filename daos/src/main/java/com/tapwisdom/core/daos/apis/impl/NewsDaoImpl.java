package com.tapwisdom.core.daos.apis.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.NewsDao;
import com.tapwisdom.core.daos.documents.News;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsDaoImpl extends BaseDaoImpl<News> implements NewsDao {
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final int MAX_NUM_ITEMS = Integer.parseInt(reader.getProperty(Constants.MAX_RES_IN_PAGE, "50"));

    @Override
    public List<News> getNewsList(String companyId) throws TapWisdomException {
        return getNewsList(companyId, 0);
    }

    @Override
    public List<News> getNewsList(List<String> companyIds) throws TapWisdomException {
        return getNewsList(companyIds, 0);
    }

    @Override
    public List<News> getNewsList(String companyId, int page) throws TapWisdomException {
        Query query = new Query(
                Criteria.where("companies").elemMatch(Criteria.where("id").is(companyId))
        );
        query.with(new PageRequest(page, MAX_NUM_ITEMS));
        List<News> newsList = operations.find(query, News.class);
        return newsList;
    }

    @Override
    public List<News> getNewsList(List<String> companyIds, int page) throws TapWisdomException {
        Query query = new Query(
                Criteria.where("companies").elemMatch(Criteria.where("id").in(companyIds))
        );
        query.with(new PageRequest(page, MAX_NUM_ITEMS));
        List<News> newsList = operations.find(query, News.class);
        return newsList;
    }

    @Override
    public boolean updateNews(News news) throws TapWisdomException {
        if (news.getId() != null) {
            Query query = new Query(Criteria.where("id").is(news.getId()));
            Update update = new Update();
            if (news.getTitle() != null) {
                update.set("title", news.getTitle());
            }
            if (news.getImageUrl() != null) {
                update.set("imageUrl", news.getImageUrl());
            }
            if (news.getImageAttribution() != null) {
                update.set("imageAttribution", news.getImageAttribution());
            }
            if (news.getContent() != null) {
                update.set("content", news.getContent());
            }
            if (news.getCompanies() != null) {
                update.set("companies", news.getCompanies());
            }
            if (news.getContentAttribution() != null) {
                update.set("contentAttribution", news.getContentAttribution());
            }
            if (news.getUpdatedAt() != null) {
                update.set("updatedAt", news.getUpdatedAt());
            }
            return operations.updateFirst(query, update, News.class).isUpdateOfExisting();
        } else {
            throw new TapWisdomException(1, "news id is mandatory for updating news");
        }
    }
}
