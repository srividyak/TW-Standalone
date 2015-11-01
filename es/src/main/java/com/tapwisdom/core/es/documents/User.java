package com.tapwisdom.core.es.documents;

import com.tapwisdom.core.daos.documents.UserView;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created by srividyak on 11/07/15.
 */
@Document(indexName = "tw", type = "user")
public class User extends BaseEntity {

    @Field(type = FieldType.Nested)
    private UserView userView;

    public UserView getUserView() {
        return userView;
    }

    public void setUserView(UserView userView) {
        this.userView = userView;
    }
}
