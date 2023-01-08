package com.campfire.campafar.Entity;

import com.campfire.campafar.Enum.PurchaseStateEnum;
import com.campfire.campafar.Enum.PurchaseTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Purchase {
    private Integer purchaseId;
    private Integer userId;
    private PurchaseTypeEnum targetType;
    private Integer targetId;
    private Date purchaseCreatedAt;
    private PurchaseStateEnum purchaseState;
    private Date purchaseFinishedAt;
    private String purchaseSerialId; //not used

    public Purchase(int uid, PurchaseTypeEnum type, int targetId) {
        this.userId = uid;
        this.targetType = type;
        this.targetId = targetId;
        this.purchaseSerialId = "";
    }
}
