package com.campfire.campafar.Controller;

import com.campfire.campafar.Entity.Purchase;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.CommonPageState;
import com.campfire.campafar.Enum.PurchaseTypeEnum;
import com.campfire.campafar.Enum.UserStateEnum;
import com.campfire.campafar.Repository.PurchaseRepository;
import com.campfire.campafar.Repository.UserRepository;
import com.campfire.campafar.Utils.InfoParser;
import com.campfire.campafar.Utils.RequestResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class PurchaseController {
    //支付及相关信息的接口
    @Resource
    PurchaseRepository purchaseRepository;
    @Resource
    UserRepository userRepository;
    @Resource
    ObjectMapper objectMapper;

    @RequestMapping("/purchase/create")
    public String createPurchase(@RequestParam(value = "uid", defaultValue = "")String uidStr,
                                 @RequestParam(value = "type", defaultValue = "")String type,
                                 @RequestParam(value = "tid", defaultValue = "")String tidStr,
                                 @RequestParam(value = "value", defaultValue = "")String valueStr) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if (uid == null) {
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        Integer tid = InfoParser.parseInt(tidStr);
        if (tid == null) {
            //对象id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        if(valueStr == null) {
            //费用参数为空
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        BigDecimal price;
        try {
            price = new BigDecimal(valueStr);
        } catch (NumberFormatException e) {
            //数字格式错误
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED,1,null));
        }

        User user = userRepository.selectUserById(uid);
        if (user == null || !user.getUserHasActivated() || user.getUserState() == UserStateEnum.LOGOFF) {
            //目标用户不存在或无权限进行此操作
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        Purchase newPurchase = new Purchase(uid, null, tid, price);
        switch (type) {
            case "article" -> newPurchase.setTargetType(PurchaseTypeEnum.ARTICLE);
            case "question" -> newPurchase.setTargetType(PurchaseTypeEnum.QUESTION);
            case "listen" -> newPurchase.setTargetType(PurchaseTypeEnum.LISTEN);
            default -> {
                //错误的类型
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
            }
        }

        switch (purchaseRepository.userCreatePurchase(user, newPurchase)) {
            case 0 -> {
                //创建成功
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, newPurchase.getPurchaseId()));
            }
            case -1 -> {
                //服务器错误
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR, 1, null));
            }
            case -2 -> {
                //已经支付过同项目
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
            }
            case -3 -> {
                //上一订单仍没有超时
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 4, null));
            }
            default -> {
                //不应执行此处
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR, 0, null));
            }
        }
    }

    @RequestMapping("/purchase/finish")
    public String finishPurchase(@RequestParam(value = "uid")String uidStr,
                                 @RequestParam(value = "pid")String pidStr) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if (uid == null) {
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        Integer pid = InfoParser.parseInt(pidStr);
        if (pid == null) {
            //订单id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        User user = userRepository.selectUserById(uid);
        if (user == null || !user.getUserHasActivated() || user.getUserState() == UserStateEnum.LOGOFF) {
            //目标用户不存在或无权限进行此操作
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        Purchase purchase = purchaseRepository.selectPurchaseByPid(pid);
        if(purchase == null) {
            //订单不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        if(!purchase.getUserId().equals(user.getUserId())) {
            //访问者和创建者不同
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.ACCESS_DENIED, 0, null));
        }

        if(user.getUserPoint().compareTo(purchase.getPurchaseValue()) < 0) {
            //余额不足
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 4, null));
        }

        switch (purchaseRepository.userExecutePurchase(user, purchase)) {
            case 0 -> {
                //支付成功
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, user.getUserPoint()));
            }
            case -2 -> {
                //订单已超时
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 5, null));
            }
            default -> {
                //服务器错误
                return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR, 0, null));
            }
        }
    }

    @RequestMapping("/purchase/cancel")
    public String cancelPurchase(@RequestParam(value = "uid", defaultValue = "")String uidStr,
                                 @RequestParam(value = "pid", defaultValue = "")String pidStr) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if (uid == null) {
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        Integer pid = InfoParser.parseInt(pidStr);
        if (pid == null) {
            //订单id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        User user = userRepository.selectUserById(uid);
        if (user == null || !user.getUserHasActivated() || user.getUserState() == UserStateEnum.LOGOFF) {
            //目标用户不存在或无权限进行此操作
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        Purchase purchase = purchaseRepository.selectPurchaseByPid(pid);
        if(purchase == null) {
            //订单不存在
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 3, null));
        }

        if(!purchase.getUserId().equals(user.getUserId())) {
            //访问者和创建者不同
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.ACCESS_DENIED, 0, null));
        }

        if(!purchaseRepository.userCancelPurchase(uid, purchase)) {
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.INTERNAL_ERROR, 0, null));
        }

        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, null));
    }

    @RequestMapping("/purchases")
    public String getPurchases(@RequestParam(value = "uid", defaultValue = "")String uidStr) throws JsonProcessingException {
        Integer uid = InfoParser.parseInt(uidStr);
        if (uid == null) {
            //用户id无效
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 1, null));
        }

        User user = userRepository.selectUserById(uid);
        if (user == null || !user.getUserHasActivated() || user.getUserState() == UserStateEnum.LOGOFF) {
            //目标用户不存在或无权限进行此操作
            return objectMapper.writeValueAsString(new RequestResult(CommonPageState.FAILED, 2, null));
        }

        List<Purchase> result = purchaseRepository.selectPurchasesOfUid(uid);
        return objectMapper.writeValueAsString(new RequestResult(CommonPageState.SUCCESSFUL, 0, result));
    }
}
