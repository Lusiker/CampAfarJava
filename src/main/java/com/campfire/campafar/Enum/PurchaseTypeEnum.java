package com.campfire.campafar.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

public enum PurchaseTypeEnum implements IEnum<Byte> {
    ARTICLE(0), //文章
    QUESTION(1), //提问
    LISTEN(2); //旁听

    @EnumValue
    private final Byte value;

    PurchaseTypeEnum(int value) {
        this.value = (byte)value;
    }

    @Override
    public Byte getValue() {
        return this.value;
    }
}
