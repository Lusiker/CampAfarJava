package com.campfire.campafar.Controller;

import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Service.UserService;
import com.campfire.campafar.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/userinfo")
public class ProfileController {
    @Autowired
    private UserService userService;
    //用户相关接口
    /**
     * 根据id查询用户信息
     * @param userId
     * @return
     */
    @GetMapping("check/{userId}")
    public R<User> getById(@PathVariable Integer userId){
        log.info("根据id查询用户信息...");
        User user = userService.getById(userId);
        if(user != null){
            return R.success(user);
        }
        return R.error("没有查询到对应用户信息");
    }
}
