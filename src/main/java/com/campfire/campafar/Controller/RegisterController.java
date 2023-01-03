package com.campfire.campafar.Controller;

import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Utils.RequestResult;
import com.campfire.campafar.Utils.StringEncoder;
import com.campfire.campafar.Utils.TempInfoGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class RegisterController {
    //注册相关接口
    @Resource
    UserRepository userRepository;
    @Resource
    ObjectMapper objectMapper;

    @RequestMapping(value = "/register")
    public String userRegister(@RequestParam(value = "email",defaultValue = "")String newUserEmail,
                               @RequestParam(value = "password",defaultValue = "")String newPassword) throws JsonProcessingException {
        if(newUserEmail.length() != 0 && newPassword.length() != 0){
            if(newUserEmail.length() > 50){
                //邮箱参数错误
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
            }

            if(newPassword.length() > 20){
                //密码参数错误
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
            }

            if(userRepository.checkUserEmailDuplicated(newUserEmail)){
                //邮箱重复
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,2,null));
            }

            //进入注册
            //记录当前时间
            Date now = new Date();
            //生成临时用户名
            String newUserName = TempInfoGenerator.generateTempUsername(now,newUserEmail);
            //生成加密后的密码
            String encodedPassword = StringEncoder.encode(newPassword);

            //创建新用户的对象
            User registeringUser = new User(newUserName,newUserEmail,encodedPassword);
            if(userRepository.createUser(registeringUser) != 0){
                //数据库操作失败，返回内部错误
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR,1,null));
            }else{
                //注册成功
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL,0,null));
            }
        }else{
            //有参数为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }
    }
}
