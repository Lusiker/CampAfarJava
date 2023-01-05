package com.campfire.campafar.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;


public enum RecordTypeEnum implements IEnum<Byte> {
    REGISTRY(0),//注册用验证码
    FORGET_PASSWORD(1),//忘记密码用验证码
    CHARGE(2);//充值用验证码

    @EnumValue
    private final Byte value;

    RecordTypeEnum(int value) {
        this.value = (byte)value;
    }

    @Override
    public Byte getValue() {
        return this.value;
    }
}
