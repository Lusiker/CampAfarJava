package com.campfire.campafar.Service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Mapper.UserMapper;
import com.campfire.campafar.Service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
}
