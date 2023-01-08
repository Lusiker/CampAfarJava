package com.campfire.campafar.Enum;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;

public enum PurchaseStateEnum implements IEnum<Byte> {
    CREATED(0),//订单创建
    FINISHED(1),//订单完成
    CANCELLED(2),//订单取消
    TIMEOUT(3);//订单超时

    @EnumValue
    private final Byte value;

    PurchaseStateEnum(int value) {
        this.value = (byte)value;
    }

    @Override
    public Byte getValue() {
        return this.value;
    }
}