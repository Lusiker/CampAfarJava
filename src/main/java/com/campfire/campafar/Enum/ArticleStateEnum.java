package com.campfire.campafar.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

public enum ArticleStateEnum implements IEnum<Byte> {
    ReVIEWING(0),//尚未确认邮箱地址可用，受限
    NORMAL(1),//文章状态正常
    DELETED(2);//文章已删除

    @EnumValue
    private final Byte value;

    ArticleStateEnum(int value) {
        this.value = (byte)value;
    }

    @Override
    public Byte getValue() {
        return this.value;
    }
}
