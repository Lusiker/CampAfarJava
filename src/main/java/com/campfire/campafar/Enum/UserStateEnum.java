package com.campfire.campafar.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

public enum UserStateEnum implements IEnum<Byte> {
    RESTRICTED(0),//用户尚未确认邮箱地址可用，受限
    NORMAL(1),//用户状态正常
    BANNED(2),//用户被封禁
    LOGOFF(3);//用户已注销

    @EnumValue
    private final Byte value;

    UserStateEnum(int value) {
        this.value = (byte)value;
    }

    @Override
    public Byte getValue() {
        return this.value;
    }
}
