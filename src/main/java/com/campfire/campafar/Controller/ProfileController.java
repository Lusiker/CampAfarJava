package com.campfire.campafar.Controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campfire.campafar.DTO.UserInfoWrapper;
import com.campfire.campafar.DTO.VisitorInfoWrapper;
import com.campfire.campafar.Entity.Article;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Mapper.UserMapper;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Service.ArticleService;
import com.campfire.campafar.Utils.InfoParser;
import com.campfire.campafar.Utils.RequestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/user")
public class ProfileController {
    //用户信息相关接口
    @Autowired
    private UserMapper userMapper;
    @Resource
    UserRepository userRepository;
    @Resource
    ObjectMapper objectMapper;
    /**
    根据userId更新用户字段
     **/
    @RequestMapping("/update")
    public String userUpdateInfo(@RequestParam(value = "userName",defaultValue = "")String newUserName,
                                 @RequestParam(value = "userEmail",defaultValue = "")String newUserEmail,
                                 @RequestParam(value = "userIntroduction",defaultValue = "")String newUserIntroduction,
                                 @RequestParam(value = "uid",defaultValue = "-1")Integer userId)throws JsonProcessingException {
        //userId为空
        if(userId==-1) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
        //邮箱输入不合法
        if(newUserEmail.length() > 50) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
        //简介输入不合法
        if(newUserIntroduction.length() > 250) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        //根据userId更新用户字段
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id",userId);
        User user = new User();
        if (!newUserEmail.equals("")) {                         //设置新邮箱
            user.setUserEmail(newUserEmail);
        }
        if (!Objects.equals(newUserName, "")) {                          //设置新用户名
            user.setUserName(newUserName);
        }
        if (!newUserIntroduction.equals("")) {                  //设置新简介
            user.setUserIntroduction(newUserIntroduction);
        }
        userMapper.update(user, updateWrapper);
            //修改成功
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
    }


    /**
     根据userId更新用户密码
     **/
    @RequestMapping("password")
    public String userCheckInfo(@RequestParam(value = "uid",defaultValue = "-1")Integer userId,
                                @RequestParam(value = "oldPassword")String oldPassword,
                                @RequestParam(value = "newPassword")String newPassword)throws JsonProcessingException{
        User user = userRepository.selectUserById(userId);
        if(newPassword.length() > 20){
            //密码参数错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
        //将上传的旧密码进行md5加密
        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if(userId==-1) {
            //userId为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
        if (!oldPassword.equals(user.getUserPassword())) {
            //旧密码不一致
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
        }
        //将上传的新密码进行md5加密
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        //根据userId更新用户密码
        user.setUserPassword(newPassword);
        userRepository.updateUser(user);
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
    }

    /**
     忘记密码
     **/
    @RequestMapping("forgetPassword")
    public String forgetPassword(@RequestParam(value = "uid",defaultValue="")Integer userId) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
    }

    /**
    忘记密码后设置新密码
     **/
    @RequestMapping("retrievePassword")
    public String retrievePassword() throws JsonProcessingException {
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
    }


    /**
     获取用户页面详情
     **/
    @RequestMapping
    public String userInfo(@RequestParam(value = "uidFrom",defaultValue = "-1")Integer userIdFrom,
                           @RequestParam(value = "uidTo",defaultValue = "-1")Integer userIdTo) throws JsonProcessingException {
        if(userIdFrom==-1||userIdTo==-1) {
            //userId为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
        User user = userRepository.selectUserById(userIdTo);

        if (!userIdFrom.equals(userIdTo)) {
            VisitorInfoWrapper wrapper;
            wrapper = new VisitorInfoWrapper.VisitorInfoWrapperBuilder()
                    .setUser(user)
                    .setUserCommonInfo()
                    .setArticleCount()
                    .setFollowInfo()
                    .setUserQuestionInfo()
                    .build();;
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,wrapper));
        }else{
            UserInfoWrapper wrapper;
            wrapper = new UserInfoWrapper.UserInfoWrapperBuilder()
                    .setUser(user)
                    .setUserCommonInfo()
                    .setUserLoginInfo()
                    .setArticleCount()
                    .setFollowInfo()
                    .setUserQuestionInfo()
                    .build();
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,wrapper));
        }
    }

    /******************
     获取用户分页的数据列表
     *****************/
    int pageSize = 10;           //每页放10条数据
    /**
     分页的文章列表
     **/
    @Autowired
    private ArticleService articleService;
    @RequestMapping("getArticles")
    public String getArticles(@RequestParam(value = "uidFrom",defaultValue = "-1")Integer userIdFrom,
                              @RequestParam(value = "uidTo",defaultValue = "-1")Integer userIdTo,
                              @RequestParam(value = "page",defaultValue = "3")Integer page,//第几页
                              @RequestParam(value = "orderBy",defaultValue = "0")Integer orderBy)throws JsonProcessingException{


        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        if (!Objects.equals(userIdFrom, userIdTo)){
            queryWrapper.eq(Article::getUserId,userIdTo)
                    .select(Article::getArticleTitle,Article::getArticleCreatedAt,Article::getArticleTag,Article::getArticleIsFree,Article::getArticlePrice,Article::getArticleDetail);
        }else{
            queryWrapper.like(StringUtils.isNotEmpty(String.valueOf(userIdTo)),Article::getUserId,userIdTo);
        }
        //添加排序条件
        if (orderBy == 0){
            queryWrapper.orderByAsc(Article::getArticleId);
        }
        if (orderBy == 1){
            queryWrapper.orderByDesc(Article::getArticleId);
        }
        if (orderBy == 2){
            queryWrapper.orderByAsc(Article::getArticleViewCount);
        }
        if (orderBy == 3){
            queryWrapper.orderByDesc(Article::getArticleViewCount);
        }
        //执行查询
        articleService.page(pageInfo,queryWrapper);

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,pageInfo));
    }

    @RequestMapping("/cardInfo")
    public String getUserCardInfo(@RequestParam(value = "uid",defaultValue = "")String uidStr) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if (uid == null) {
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        User user = userRepository.selectUserById(uid);
        if (user == null) {
            //目标用户不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        UserInfoWrapper wrapper = new UserInfoWrapper.UserInfoWrapperBuilder()
                .setUser(user)
                .setUserCardInfo()
                .setUserLoginInfo()
                .setUserAvatar()
                .build();
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, wrapper));
    }
}

