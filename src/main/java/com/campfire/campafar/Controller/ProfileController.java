package com.campfire.campafar.Controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campfire.campafar.DTO.UserInfoWrapper;
import com.campfire.campafar.DTO.VisitorInfoWrapper;
import com.campfire.campafar.Entity.Article;
import com.campfire.campafar.Entity.Record;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Enum.RecordTypeEnum;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Mapper.UserMapper;
import com.campfire.campafar.Repository.RecordRepository;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Service.ArticleService;
import com.campfire.campafar.Utils.InfoParser;
import com.campfire.campafar.Utils.MailProcessor;
import com.campfire.campafar.Utils.RequestResult;
import com.campfire.campafar.Utils.TempInfoGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
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
    RecordRepository recordRepository;
    @Resource
    ObjectMapper objectMapper;
    @Resource
    MailProcessor processor;

    private final int activateCodeExpireTime = 5 * 60 * 1000;

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
        if(userId==-1) {
            //userId为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        if(newPassword.length() > 20){
            //密码参数错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        User user = userRepository.selectUserById(userId);
        if(user == null){
            //目标用户不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }
        //将上传的旧密码进行md5加密
        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        if (!oldPassword.equals(user.getUserPassword())) {
            //旧密码不一致
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,2,null));
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
        if(user == null) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,2,null));
        }

        if (!userIdFrom.equals(userIdTo)) {
            VisitorInfoWrapper wrapper;
            wrapper = new VisitorInfoWrapper.VisitorInfoWrapperBuilder()
                    .setUser(user)
                    .setUserCommonInfo()
                    .setArticleCount()
                    .setFollowInfo()
                    .setUserQuestionInfo()
                    .build();
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

    @RequestMapping("/activate")
    public String requestActivateCode(@RequestParam(value = "uid")String uidStr) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if(uid == null){
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        User user = userRepository.selectUserById(uid);
        if(user == null || user.getUserState() == UserStateEnum.LOGOFF){
            //目标用户不存在或已注销
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        if(user.getUserHasActivated()) {
            //用户已激活，无法再次请求
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        //查询先前请求记录
        Record prevRecord = recordRepository.selectRecordWithUidAndType(user.getUserId(),RecordTypeEnum.ACTIVATION);
        //记录当前时间
        Date now = new Date();
        if(prevRecord == null) {
            //记录不存在
            //生成随机的四位激活码
            String tempCode = TempInfoGenerator.getRandomFourDigitNumber(now.getTime());
            Record newRecord = new Record(user.getUserId(), RecordTypeEnum.ACTIVATION, tempCode);
            //创建记录到数据库
            if(!recordRepository.insertRecord(newRecord)) {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
            }

            //发送邮件，返回成功状态
            processor.sendRegistryCode(tempCode, user.getUserName(), user.getUserEmail(), uid);
        } else {
            if(now.getTime() - prevRecord.getCreatedAt().getTime() < 6000) {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.REQUEST_TOO_FREQUENT, 0, null));
            }

            //记录存在但已超过一分钟
            //生成随机的四位激活码
            //更新验证码和创建时间
            String tempCode = TempInfoGenerator.getRandomFourDigitNumber(now.getTime());
            prevRecord.setRecordCode(tempCode);
            prevRecord.setCreatedAt(now);

            if(!recordRepository.updateRecord(prevRecord)) {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
            }

            //发送邮件，返回成功状态
            processor.sendRegistryCode(tempCode, user.getUserName(), user.getUserEmail(), uid);
        }

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0, null));
    }

    @RequestMapping("/checkActivate")
    public String checkActivateCode(@RequestParam(value = "uid",defaultValue = "")String uidStr,
                                    @RequestParam(value = "code")String code) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if(uid == null){
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        if(code.isEmpty() || code.length() > 4) {
            //验证法不合法
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        User user = userRepository.selectUserById(uid);
        if(user == null || user.getUserState() == UserStateEnum.LOGOFF){
            //目标用户不存在或已注销
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        if(user.getUserHasActivated()) {
            //用户已激活
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        //查找记录
        Record record = recordRepository.selectRecordWithUidAndType(user.getUserId(),RecordTypeEnum.ACTIVATION);
        if(record == null) {
            //激活码未发送
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 5, null));
        }

        Date now = new Date();
        if(now.getTime() - record.getCreatedAt().getTime() > activateCodeExpireTime) {
            //激活码已超时
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 5, null));
        }

        if(code.equals(record.getRecordCode())){
            //激活用户
            if(!userRepository.activateUser(user)) {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
            }
            //删除旧记录
            if(!recordRepository.deleteRecord(record)) {
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
            }

            //激活成功
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0, null));
        }

        //激活码错误
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 4, null));
    }
}

