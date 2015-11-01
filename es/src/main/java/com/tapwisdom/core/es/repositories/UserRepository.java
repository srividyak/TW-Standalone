package com.tapwisdom.core.es.repositories;

import com.tapwisdom.core.es.documents.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Created by srividyak on 11/07/15.
 */
public interface UserRepository extends ElasticsearchRepository<User, String> {
    
    List<User> getUserById(String id);
    
}
