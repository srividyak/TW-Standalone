package com.tapwisdom.core.es.repositories;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.es.UserSearchCriteria;
import com.tapwisdom.core.es.documents.User;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.elasticsearch.index.query.FilterBuilders.*;
/**
 * Created by srividyak on 16/07/15.
 */
@Component
public class UserSearchRepositoryImpl implements IUserSearchRepository {
    
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final int maxResults = Integer.parseInt(reader.getProperty(Constants.MAX_RES_IN_PAGE, "50"));
    
    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public User getUserByUserId(String userId) throws TapWisdomException {
        try {
            QueryBuilder queryBuilder = boolQuery().must(matchPhraseQuery("userView.id", userId));
            NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder("userView", queryBuilder);
            QueryBuilder finalQueryBuilder = boolQuery().must(nestedQueryBuilder);
            SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(finalQueryBuilder).build();
            List<User> users = template.queryForList(searchQuery, User.class);
            return users == null || users.size() == 0 ? null : users.get(0);
        } catch (Exception e) {
            throw new TapWisdomException(e.getMessage(), e);
        }
    }

    @Override
    public List<User> getUsers(UserSearchCriteria criteria, int page) throws TapWisdomException {
        try {
            String industry = criteria.getIndustry();
            MatchQueryBuilder linkedInCompanyNameQueryBuilder = matchQuery("userView.linkedInProfile.positions.company.name", criteria.getCompanyName());
            MatchQueryBuilder designationQueryBuilder = matchQuery("userView.linkedInProfile.positions.title", criteria.getDesignation());
            MatchQueryBuilder industryQueryBuilder = matchQuery("userView.linkedInProfile.positions.company.industry", industry);
            QueryBuilder queryBuilder = boolQuery()
                                        .should(linkedInCompanyNameQueryBuilder)
                                        .should(designationQueryBuilder)
                                        .should(industryQueryBuilder);
            NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder("userView", queryBuilder);
            QueryBuilder finalQueryBuilder = boolQuery().must(nestedQueryBuilder);
            SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(finalQueryBuilder).build();
            searchQuery.addSort(new Sort(Sort.Direction.DESC, "_score"))
                    .addSort(new Sort(Sort.Direction.ASC, "userView.numQuestionsAnswered"))
                    .setPageable(new PageRequest(page, maxResults));
            return template.queryForList(searchQuery, User.class);
        } catch (Exception e) {
            throw new TapWisdomException(e.getMessage(), e);
        }
    }
}
