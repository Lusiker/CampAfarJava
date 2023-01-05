package com.campfire.campafar.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campfire.campafar.DTO.UserInfoWrapper;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Service.UserService;
import com.campfire.campafar.Utils.RequestResult;
import com.campfire.campafar.common.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Slf4j
@RestController
@RequestMapping("/login")
/**
 * 用户登录
 * **/
public class LoginController {
    @Autowired
    private UserService userService;
    //注册相关接口
    @Resource
    UserRepository userRepository;
    @Resource
    ObjectMapper objectMapper;
    @PostMapping("/usernameLogin")
    /**
     * 用户名登录
     **/
    public R<UserInfoWrapper> usernameLogin(@RequestBody User user) {
        //1、将页面提交的密码password进行md5加密处理
        String password = user.getUserPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,user.getUserName());
        user = userService.getOne(queryWrapper);

        //3、如果没有查询到则返回登录失败结果
        if(user == null){
            return R.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!user.getUserPassword().equals(password)){
            return R.error("登录失败");
        }

        //5、查看用户状态，如果为注销状态，则返回用户已禁用结果
        if(user.getUserState() == UserStateEnum.LOGOFF){
            return R.error("账号已注销");
        }

        //登录时间，登录总数更新
        Date now = new Date();
        userRepository.updateLogin(user, now);

        UserInfoWrapper wrapper = new UserInfoWrapper.UserInfoWrapperBuilder()
                .setUser(user)
                .setUserCommonInfo()
                .setUserLoginInfo()
                .setArticleCount()
                .setFollowInfo()
                .setUserQuestionInfo()
                .build();

        return R.success(wrapper);
    }

    @RequestMapping("/email")
    public String userLoginByEmail(@RequestParam(value = "email", defaultValue = "")String userEmail,
                                   @RequestParam(value = "password", defaultValue = "")String password) throws JsonProcessingException {
        //任意参数为空
        if(userEmail.isEmpty() || password.isEmpty()) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        //邮箱输入不合法
        if(userEmail.length() > 50) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        //密码输入不合法
        if(password.length() < 6 || password.length() > 20) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        //查找目标用户
        User user = userRepository.selectUserByEmail(userEmail);
        //用户不存在或已注销
        if(user == null || user.getUserState() == UserStateEnum.LOGOFF){
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,2,null));
        }


        String encodedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if(encodedPassword.equals(user.getUserPassword())) {
            //回传用户信息
            //登录时间，登录总数更新
            Date now = new Date();
            userRepository.updateLogin(user, now);

            UserInfoWrapper wrapper = new UserInfoWrapper.UserInfoWrapperBuilder()
                    .setUser(user)
                    .setUserCommonInfo()
                    .setUserLoginInfo()
                    .setArticleCount()
                    .setFollowInfo()
                    .setUserQuestionInfo()
                    .build();

            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,wrapper));
        } else {
            //密码错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,3,null));
        }
    }
}
