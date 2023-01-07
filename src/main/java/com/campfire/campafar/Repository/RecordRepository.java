package com.campfire.campafar.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.campfire.campafar.Entity.Record;
import com.campfire.campafar.Enum.RecordTypeEnum;
import com.campfire.campafar.Mapper.RecordMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class RecordRepository {
    @Resource
    RecordMapper recordMapper;

    public boolean insertRecord(Record newRecord){
        return recordMapper.insert(newRecord) == 1;
    }

    public boolean updateRecord(Record newRecord) {
        UpdateWrapper<Record> wrapper = new UpdateWrapper<Record>().eq("record_id", newRecord.getRecordId());

        return recordMapper.update(newRecord, wrapper) == 1;
    }

    public boolean deleteRecord(Record record) {
        return recordMapper.deleteById(record) == 1;
    }

    public Record selectRecordWithUidAndType(int uid, RecordTypeEnum typeEnum) {
        QueryWrapper<Record> wrapper = new QueryWrapper<Record>()
                .eq("uid",uid)
                .and(w -> w.eq("record_type", typeEnum));

        return recordMapper.selectOne(wrapper);
    }
}
