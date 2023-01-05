package com.campfire.campafar.Controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Service.UserService;
import com.campfire.campafar.common.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


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
    public R<User> usernameLogin(HttpServletRequest request, @RequestBody User user) throws JsonProcessingException {
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
        return R.success(user);
    }
}
