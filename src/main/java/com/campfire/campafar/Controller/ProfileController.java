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
@RequestMapping("/user")
public class ProfileController {
    @Autowired
    private UserService userService;
    //用户相关接口
}
