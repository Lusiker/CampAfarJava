package com.campfire.campafar.Enum;

public enum CommonPageState implements ControllerStateEnum {
    SUCCESSFUL(0,"successful"),
    FAILED(-1,"failed"),
    INTERNAL_ERROR(1,"internal error"),
    ACCESS_DENIED(-2,"access denied"),
    REQUEST_TOO_FREQUENT(-3,"request too frequent"),
    STATE_ERROR(-4,"state error");

    private final Integer stateCode;
    private final String value;

    CommonPageState(Integer stateCode, String value) {
        this.stateCode = stateCode;
        this.value = value;
    }

    @Override
    public int getStateCode(){
        return this.stateCode;
    }

    @Override
    public String getValue() {
        return this.value;
    }
}

