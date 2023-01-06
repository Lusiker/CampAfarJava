package com.campfire.campafar.DTO;

import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Utils.DateProcessor;
import com.campfire.campafar.Utils.FileProcessor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
public class UserInfoWrapper {
    private Integer userId;
    private String userName;
    private String userEmail;
    private String userIntroduction;
    private Date lastLogin;
    private String lastLoginString;
    private Boolean userHasActivated;
    private UserStateEnum userState;
    private Integer userExp;
    private BigDecimal userPoint;
    private Boolean userQuestionOpen;
    private BigDecimal userQuestionPrice;
    private Integer userArticleCount;
    private Integer userFollowerCount;
    private Integer userFollowingCount;
    private String userAvatar;

    public UserInfoWrapper(UserInfoWrapperBuilder builder) {
        this.userId = builder.getBUserId();
        this.userEmail = builder.getBUserEmail();
        this.userName = builder.getBUserName();
        this.userIntroduction = builder.getBUserIntroduction();
        this.lastLogin = builder.getBLastLogin();
        this.lastLoginString = builder.getBLastLoginString();
        this.userHasActivated = builder.getBUserHasActivated();
        this.userState = builder.getBUserState();
        this.userExp = builder.getBUserExp();
        this.userPoint = builder.getBUserPoint();
        this.userQuestionOpen = builder.getBUserQuestionOpen();
        this.userQuestionPrice = builder.getBUserQuestionPrice();
        this.userArticleCount = builder.getBUserArticleCount();
        this.userFollowerCount = builder.getBUserFollowerCount();
        this.userFollowingCount = builder.getBUserFollowingCount();
        this.userAvatar = builder.getBUserAvatar();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UserInfoWrapperBuilder {
        private User user;
        private Integer bUserId;
        private String bUserName;
        private String bUserEmail;
        private String bUserIntroduction;
        private Date bLastLogin;
        private String bLastLoginString;
        private Boolean bUserHasActivated;
        private UserStateEnum bUserState;
        private Integer bUserExp;
        private BigDecimal bUserPoint;
        private Boolean bUserQuestionOpen;
        private BigDecimal bUserQuestionPrice;
        private Integer bUserArticleCount;
        private Integer bUserFollowerCount;
        private Integer bUserFollowingCount;
        private String bUserAvatar;

        public UserInfoWrapperBuilder setUser(User user) {
            this.user = user;

            return this;
        }

        public UserInfoWrapperBuilder setUserCommonInfo() {
            this.bUserId = user.getUserId();
            this.bUserName = user.getUserName();
            this.bUserEmail = user.getUserEmail();
            this.bUserState = user.getUserState();
            this.bUserPoint = user.getUserPoint();
            this.bUserExp = user.getUserExp();
            this.bUserIntroduction = user.getUserIntroduction();
            this.bUserHasActivated = user.getUserHasActivated();

            return this;
        }

        public UserInfoWrapperBuilder setUserCardInfo() {
            this.bUserId = user.getUserId();
            this.bUserName = user.getUserName();
            this.bUserEmail = user.getUserEmail();
            this.bUserState = user.getUserState();

            return this;
        }

        public UserInfoWrapperBuilder setUserAvatar() {
            this.bUserAvatar = FileProcessor.getAvatarAsBase64(this.bUserId);

            return this;
        }

        public UserInfoWrapperBuilder setUserQuestionInfo() {
            this.bUserQuestionOpen = user.getUserQuestionOpen();
            this.bUserQuestionPrice = user.getUserQuestionPrice();

            return this;
        }

        public UserInfoWrapperBuilder setUserLoginInfo() {
            this.bLastLogin = this.user.getLastLogin();
            this.bLastLoginString = DateProcessor.getLastLoginInfo(this.bLastLogin);

            return this;
        }

        public UserInfoWrapperBuilder setArticleCount() {
            //to be implemented
            this.bUserArticleCount = 0;

            return this;
        }

        public UserInfoWrapperBuilder setFollowInfo() {
            //to be implemented
            this.bUserFollowerCount = 0;
            this.bUserFollowingCount = 0;

            return this;
        }

        public UserInfoWrapper build(){
            return new UserInfoWrapper(this);
        }
    }
}
