package com.campfire.campafar.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.campfire.campafar.Enum.UserStateEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TableName(schema = "campafar",value = "user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String userName;
    private String userEmail;
    private String userPassword;
    private String userIntroduction;
    private Date createdAt;
    private Date lastLogin;
    private Integer totalLogin;
    private Boolean userHasActivated;
    private UserStateEnum userState;
    private Integer userExp;
    private BigDecimal userPoint;
    private Boolean userQuestionOpen;
    private BigDecimal userQuestionPrice;

    public User(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userState = UserStateEnum.RESTRICTED;
    }
}
