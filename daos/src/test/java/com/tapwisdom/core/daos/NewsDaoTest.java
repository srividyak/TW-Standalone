package com.tapwisdom.core.daos;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.NewsDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.News;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class NewsDaoTest {
    
    @Autowired
    private NewsDao newsDao;
    
    @Autowired
    private MongoOperations operations;
    
    private News news;
    
    @Before
    public void create() {
        news = new News();
        Company company = new Company();
        company.setId("tapwisdom");
        news.setCompanies(new Company[]{company});
        news.setContent("TapWisdom is a great company");
        news.setTitle("TapWisdom is cool");
        operations.save(news);
    }
    
    @Test
    public void testGetNewsFromExistingCompany() {
        try {
            List<News> newsList = newsDao.getNewsList("tapwisdom");
            assert newsList != null;
            assert newsList.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetNewsFromNonExistingCompany() {
        try {
            List<News> newsList = newsDao.getNewsList("tw");
            assert newsList != null;
            assert newsList.size() == 0;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testUpdateNews() {
        String imageUrl = "image";
        news.setImageUrl(imageUrl);
        try {
            boolean isUpdated = newsDao.updateNews(news);
            assert isUpdated;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetNewsByMultipleCompanies() {
        // adding one more company
        News secondNews = new News();
        Company company = new Company();
        company.setId("flipkart");
        secondNews.setCompanies(new Company[]{company});
        secondNews.setContent("flipkart is a great company");
        secondNews.setTitle("flipkart is cool");
        operations.save(secondNews);
        List<String> companyIds = Arrays.asList("tapwisdom", "flipkart");
        try {
            List<News> newsList = newsDao.getNewsList(companyIds);
            assert newsList.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void delete() {
        operations.findAllAndRemove(new Query(), News.class);
    }
    
}
