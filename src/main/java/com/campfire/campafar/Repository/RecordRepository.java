package com.campfire.campafar.Repository;

import com.campfire.campafar.Mapper.RecordMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class RecordRepository {
    @Resource
    RecordMapper recordMapper;

}
