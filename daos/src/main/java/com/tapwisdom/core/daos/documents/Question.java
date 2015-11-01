package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by srividyak on 04/07/15.
 */
@Document(collection = "question")
public class Question extends BaseEntity {
    
    // comma separated tags
    @TextIndexed
    private String tags;
    
    private String[] industries;
    private QuestionType type;
    private Object content;
    
    @Indexed
    private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String[] getIndustries() {
        return industries;
    }

    public void setIndustries(String[] industries) {
        this.industries = industries;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
