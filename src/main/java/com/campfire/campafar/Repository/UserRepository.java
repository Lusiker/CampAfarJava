package com.campfire.campafar.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Repository
public class UserRepository {
    //用户相关的数据库操作
    @Resource
    UserMapper userMapper;

    public boolean insertUser(User newUser){
        return userMapper.insert(newUser) == 1;
    }

    public boolean updateUser(User user){
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id",user.getUserId());

        return userMapper.update(user, wrapper) == 1;
    }

    //basic delete
    public boolean deleteUser(User user){
        //ALERT: Under NO circumstance shall this function be called
        QueryWrapper<User> wrapper = new QueryWrapper<User>()
                .eq("user_id",user.getUserId());

        return userMapper.delete(wrapper) == 1;
    }

    public User selectUserByEmail(String userEmail){
        //select a user by given email
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_email", userEmail);

        return userMapper.selectOne(wrapper);
    }

    public User selectUserById(int uid){
        //select a user by given uid
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_id", uid);

        return userMapper.selectOne(wrapper);
    }

    public User selectUserByUsername(String userName){
        //select a user by given username
        QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("user_name", userName);

        return userMapper.selectOne(wrapper);
    }

    public int createUser(User user){
        if(!insertUser(user)){
            return -1;
        }

        return 0;
    }

    public boolean checkUserEmailDuplicated(String newUserEmail){
        //检查用户邮箱是否重复
        User existEmail = selectUserByEmail(newUserEmail);

        return existEmail != null;
    }

    public boolean checkUserNameDuplicated(String newUserName){
        //检查用户名是否重复
        User existName = selectUserByUsername(newUserName);

        return existName != null;
    }

    public boolean updateLogin(User user, Date now){
        user.setLastLogin(now);
        int loginCount = user.getTotalLogin();
        user.setTotalLogin(++loginCount);

        return updateUser(user);
    }

    public boolean activateUser(User user) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .eq("user_id", user.getUserId())
                .set("user_has_activated", true);
        if(user.getUserState() == UserStateEnum.RESTRICTED) {
            wrapper.set("user_state", UserStateEnum.NORMAL);
        }

        return userMapper.update(null, wrapper) == 1;
    }

    public boolean charge(User user, BigDecimal value) {
        user.setUserPoint(user.getUserPoint().add(value));

        UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
                .eq("user_id",user.getUserId())
                .set("user_point", user.getUserPoint());

        return userMapper.update(null, wrapper) == 1;
    }
}
