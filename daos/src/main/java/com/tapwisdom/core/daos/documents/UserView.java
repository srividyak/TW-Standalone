package com.tapwisdom.core.daos.documents;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.social.linkedin.api.LinkedInProfileFull;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserView extends BaseEntity {

    private LiProfile linkedInProfile;
    private FacebookProfile facebookProfile;
    private GoogleProfile googleProfile;
    private String phoneNumber;
    
    @Indexed
    private String email;
    private Map<String, Object> customProfile;
    private Double credits = 0.0;
    private UserStatus status;
    private UserSource source;
    
    @Indexed
    private UserRole role;
    private Long viewCount;
    private Double aggregateRating;
    private String deviceId;
    private UserPrivacy visibilityFlag;
    
    @Indexed(unique = true, sparse = true)
    private String aliasName;
    private Long lastSeen;
    private Long numQuestionsAsked;
    private Long numQuestionsAnswered;

    private MobileOs mobileOs;
    private UpvoteBadge upvoteBadge;
    private Boolean isVerifiedAdvisor;
    private EvaluateBadge evaluateBadge;
    private Long qnAUpvoteCount;
    private List<TapBadge> tapBadges;

    public UserView() {
        viewCount = 0L;
        qnAUpvoteCount = 0L;
        aggregateRating = 0.0;
        visibilityFlag = UserPrivacy.PUBLIC;
        numQuestionsAnswered = 0L;
        numQuestionsAsked = 0L;
        lastSeen = new Date().getTime();
        status = UserStatus.unsubscribed;
        role = UserRole.seeker;
        isVerifiedAdvisor = false;
    }
    
    protected UserView(boolean isEmpty) {
        if (!isEmpty) {
            throw new IllegalArgumentException("isEmpty cannot be false");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Long getNumQuestionsAsked() {
        return numQuestionsAsked;
    }

    public void setNumQuestionsAsked(Long numQuestionsAsked) {
        this.numQuestionsAsked = numQuestionsAsked;
    }

    public Long getNumQuestionsAnswered() {
        return numQuestionsAnswered;
    }

    public void setNumQuestionsAnswered(Long numQuestionsAnswered) {
        this.numQuestionsAnswered = numQuestionsAnswered;
    }

    public Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public FacebookProfile getFacebookProfile() {
        return facebookProfile;
    }

    public void setFacebookProfile(FacebookProfile facebookProfile) {
        this.facebookProfile = facebookProfile;
    }

    public GoogleProfile getGoogleProfile() {
        return googleProfile;
    }

    public void setGoogleProfile(GoogleProfile googleProfile) {
        this.googleProfile = googleProfile;
    }

    public LiProfile getLinkedInProfile() {
        return linkedInProfile;
    }

    public void setLinkedInProfile(LiProfile linkedInProfile) {
        this.linkedInProfile = linkedInProfile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getCustomProfile() {
        return customProfile;
    }

    public void setCustomProfile(Map<String, Object> customProfile) {
        this.customProfile = customProfile;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserSource getSource() {
        return source;
    }

    public void setSource(UserSource source) {
        this.source = source;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Double getAggregateRating() {
        return aggregateRating;
    }

    public void setAggregateRating(Double aggregateRating) {
        this.aggregateRating = aggregateRating;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public UserPrivacy getVisibilityFlag() {
        return visibilityFlag;
    }

    public void setVisibilityFlag(UserPrivacy visibilityFlag) {
        this.visibilityFlag = visibilityFlag;
    }

    public MobileOs getMobileOs() {
        return mobileOs;
    }

    public void setMobileOs(MobileOs mobileOs) {
        this.mobileOs = mobileOs;
    }

    public UpvoteBadge getUpvoteBadge() {
        return upvoteBadge;
    }

    public void setUpvoteBadge(UpvoteBadge upvoteBadge) {
        this.upvoteBadge = upvoteBadge;
    }

    public Long getQnAUpvoteCount() {
        return qnAUpvoteCount;
    }

    public void setQnAUpvoteCount(Long qnAUpvoteCount) {
        this.qnAUpvoteCount = qnAUpvoteCount;
    }

    public EvaluateBadge getEvaluateBadge() {
        return evaluateBadge;
    }

    public void setEvaluateBadge(EvaluateBadge evaluateBadge) {
        this.evaluateBadge = evaluateBadge;
    }

    public List<TapBadge> getTapBadges() {
        return tapBadges;
    }

    public void setTapBadges(List<TapBadge> tapBadges) {
        this.tapBadges = tapBadges;
    }

    public Boolean getIsVerifiedAdvisor() {
        return isVerifiedAdvisor;
    }

    public void setIsVerifiedAdvisor(Boolean isVerifiedAdvisor) {
        this.isVerifiedAdvisor = isVerifiedAdvisor;
    }
}
