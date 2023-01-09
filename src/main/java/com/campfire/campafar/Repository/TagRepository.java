package com.campfire.campafar.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.campfire.campafar.Entity.Tag;
import com.campfire.campafar.Mapper.TagMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class TagRepository {
    @Resource
    TagMapper tagMapper;
    //标签相关数据库操作
    public Tag selectTagById(int tid){
        //select a user by given uid
        QueryWrapper<Tag> wrapper = new QueryWrapper<Tag>().eq("tag_id", tid);

        return tagMapper.selectOne(wrapper);
    }
}
