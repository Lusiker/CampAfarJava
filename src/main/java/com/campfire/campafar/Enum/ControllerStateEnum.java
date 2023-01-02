package com.campfire.campafar.Enum;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public interface ControllerStateEnum {
    int getStateCode();
    String getValue();
}
