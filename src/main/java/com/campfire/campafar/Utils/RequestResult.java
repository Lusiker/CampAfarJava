package com.campfire.campafar.Utils;

import com.campfire.campafar.Enum.ControllerStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestResult {
    ControllerStateEnum stateEnum;
    Integer specificCode;
    Object returnObject;
}
