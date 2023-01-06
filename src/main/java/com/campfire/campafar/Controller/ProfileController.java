package com.campfire.campafar.Controller;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Mapper.UserMapper;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Utils.RequestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


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
    @PutMapping("/update")
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
    @PutMapping("password")//根据userId更新用户密码
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
}
