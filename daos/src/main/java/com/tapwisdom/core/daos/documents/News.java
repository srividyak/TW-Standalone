package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "news")
public class News extends BaseEntity {
    
    private String title;
    private String content;
    private String imageUrl;
    private String imageAttribution;
    private String contentAttribution;
    
    @Indexed
    private Company[] companies;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageAttribution() {
        return imageAttribution;
    }

    public void setImageAttribution(String imageAttribution) {
        this.imageAttribution = imageAttribution;
    }

    public String getContentAttribution() {
        return contentAttribution;
    }

    public void setContentAttribution(String contentAttribution) {
        this.contentAttribution = contentAttribution;
    }

    public Company[] getCompanies() {
        return companies;
    }

    public void setCompanies(Company[] companies) {
        this.companies = companies;
    }
}
