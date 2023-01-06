package com.campfire.campafar.DTO;


import com.campfire.campafar.Entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class VisitorInfoWrapper {
    /**
     * 访客信息包装
     * **/
    private Integer userId;
    private String userName;
    private String userEmail;
    private String userIntroduction;
    private Boolean userQuestionOpen;
    private BigDecimal userQuestionPrice;
    private Integer userArticleCount;
    private Integer userFollowerCount;
    private Integer userFollowingCount;

    public VisitorInfoWrapper(VisitorInfoWrapperBuilder builder){
        this.userId = builder.getBUserId();
        this.userEmail = builder.getBUserEmail();
        this.userName = builder.getBUserName();
        this.userIntroduction = builder.getBUserIntroduction();
        this.userQuestionOpen = builder.getBUserQuestionOpen();
        this.userQuestionPrice = builder.getBUserQuestionPrice();
        this.userArticleCount = builder.getBUserArticleCount();
        this.userFollowerCount = builder.getBUserFollowerCount();
        this.userFollowingCount = builder.getBUserFollowingCount();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class VisitorInfoWrapperBuilder {
        private User user;
        private Integer bUserId;
        private String bUserName;
        private String bUserEmail;
        private String bUserIntroduction;
        private Boolean bUserQuestionOpen;
        private BigDecimal bUserQuestionPrice;
        private Integer bUserArticleCount;
        private Integer bUserFollowerCount;
        private Integer bUserFollowingCount;

        public VisitorInfoWrapperBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public VisitorInfoWrapperBuilder setUserCommonInfo() {
            this.bUserId = user.getUserId();
            this.bUserName = user.getUserName();
            this.bUserEmail = user.getUserEmail();
            this.bUserIntroduction = user.getUserIntroduction();

            return this;
        }

        public VisitorInfoWrapperBuilder setFollowInfo() {
            //to be implemented
            this.bUserFollowerCount = 0;
            this.bUserFollowingCount = 0;

            return this;
        }

        public VisitorInfoWrapperBuilder setUserQuestionInfo() {
            this.bUserQuestionOpen = user.getUserQuestionOpen();
            this.bUserQuestionPrice = user.getUserQuestionPrice();

            return this;
        }

        public VisitorInfoWrapperBuilder setArticleCount() {
            //to be implemented
            this.bUserArticleCount = 0;

            return this;
        }

        public VisitorInfoWrapper build() {
            return new VisitorInfoWrapper(this);
        }
    }
}
