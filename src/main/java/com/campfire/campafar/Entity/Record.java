package com.campfire.campafar.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.campfire.campafar.Enum.RecordTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@TableName(schema = "campafar",value = "mail_record")
public class Record {
    @TableId(type = IdType.AUTO)
    Integer recordId;
    Integer uid;
    RecordTypeEnum recordType;
    Date createdAt;
    String recordCode;

    public Record(int uid, RecordTypeEnum type, String code) {
        this.uid = uid;
        this.recordType = type;
        this.recordCode = code;
    }
}
