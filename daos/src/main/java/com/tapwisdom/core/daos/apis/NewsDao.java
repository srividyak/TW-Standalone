package com.tapwisdom.core.daos.apis;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.News;

import java.util.List;

public interface NewsDao extends BaseDao<News> {
    
    public List<News> getNewsList(String companyId) throws TapWisdomException;
    
    public List<News> getNewsList(List<String> companyIds) throws TapWisdomException;

    public List<News> getNewsList(String companyId, int page) throws TapWisdomException;

    public List<News> getNewsList(List<String> companyIds, int page) throws TapWisdomException;
    
    public boolean updateNews(News news) throws TapWisdomException;
    
}
