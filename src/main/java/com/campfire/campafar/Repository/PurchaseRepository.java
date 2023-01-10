package com.campfire.campafar.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.campfire.campafar.Entity.Purchase;
import com.campfire.campafar.Entity.User;
import com.campfire.campafar.Enum.PurchaseStateEnum;
import com.campfire.campafar.Enum.PurchaseTypeEnum;
import com.campfire.campafar.Mapper.PurchaseMapper;
import com.campfire.campafar.Mapper.UserMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Repository
public class PurchaseRepository {
    //支付相关数据库操作
    @Resource
    PurchaseMapper purchaseMapper;
    @Resource
    UserMapper userMapper;

    //创建新的支付订单
    public boolean insertPurchase(Purchase purchase) {
        return purchaseMapper.insert(purchase) == 1;
    }

    public Purchase selectPurchaseByPid(int pid) {
        QueryWrapper<Purchase> wrapper = new QueryWrapper<Purchase>().eq("purchase_id", pid);

        return purchaseMapper.selectOne(wrapper);
    }

    public List<Purchase> selectPurchasesOfUid(int uid) {
        QueryWrapper<Purchase> wrapper = new QueryWrapper<Purchase>().eq("user_id", uid);

        List<Purchase> list = purchaseMapper.selectList(wrapper);
        Date now = new Date();
        for(Purchase p : list) {
            if(p.getPurchaseState() == PurchaseStateEnum.CREATED) {
                if (now.getTime() - p.getPurchaseCreatedAt().getTime() > 10 * 60 * 1000) {
                    //订单已超时，无法支付
                    p.setPurchaseState(PurchaseStateEnum.TIMEOUT);
                    UpdateWrapper<Purchase> updateWrapper = new UpdateWrapper<Purchase>()
                            .eq("purchase_id", p.getPurchaseId())
                            .set("purchase_state", p.getPurchaseState());

                    purchaseMapper.update(null, updateWrapper);
                }
            }
        }

        list.sort((p1, p2) -> Long.compare(p2.getPurchaseCreatedAt().getTime(), p1.getPurchaseCreatedAt().getTime()));

        return list;
    }

    public Purchase selectPurchaseByUidAndPurchaseInfo(int uid, PurchaseTypeEnum type, int tid) {
        QueryWrapper<Purchase> wrapper = new QueryWrapper<Purchase>()
                .eq("user_id", uid)
                .and(w -> w.eq("target_type", type))
                .and(w -> w.eq("target_id", tid))
                .orderBy(true, false, "purchase_created_at")
                .last("limit 1");

        return purchaseMapper.selectOne(wrapper);
    }

    public int userCreatePurchase(User user, Purchase purchase) {
        Purchase prevPurchase = selectPurchaseByUidAndPurchaseInfo(user.getUserId(), purchase.getTargetType(), purchase.getTargetId());
        if(prevPurchase == null) {
            //对于相同目标的订单不存在
            if(insertPurchase(purchase)) {
                //创建成功
                return 0;
            }

            //服务器错误,创建失败
            return -1;
        }

        //原先的同类订单存在
        switch (prevPurchase.getPurchaseState()) {
            case FINISHED -> {
                //已经支付过，无法再次创建
                return -2;
            }
            case TIMEOUT, CANCELLED -> {
                //上一订单已取消或超时，可以创建
                if(insertPurchase(purchase)) {
                    //创建成功
                    return 0;
                }

                //服务器错误,创建失败
                return -1;
            }
            case CREATED -> {
                Date now = new Date();
                if(now.getTime() - prevPurchase.getPurchaseCreatedAt().getTime() > 10 * 60 * 1000) {
                    //先前未支付订单已超时
                    UpdateWrapper<Purchase> wrapper1 = new UpdateWrapper<Purchase>().eq("purchase_id", prevPurchase.getPurchaseId());
                    wrapper1.set("purchase_state", PurchaseStateEnum.TIMEOUT);

                    //更新上一订单情况
                    if(purchaseMapper.update(null, wrapper1) != 1) {
                        //服务器错误,更新失败
                        return -1;
                    }

                    if(insertPurchase(purchase)) {
                        //创建成功
                        return 0;
                    }

                    //服务器错误,创建失败
                    return -1;
                } else {
                    //上一订单尚未超时，无法创建
                    return -3;
                }
            }
            default -> {
                //unreachable
                return -4;
            }
        }
    }

    public int userExecutePurchase(User user, Purchase purchase) {
        UpdateWrapper<Purchase> wrapper1 = new UpdateWrapper<Purchase>().eq("purchase_id", purchase.getPurchaseId());
        //当前时间
        Date now = new Date();
        if(now.getTime() - purchase.getPurchaseCreatedAt().getTime() > 10 * 60 * 1000) {
            //订单已超时，无法支付
            wrapper1.set("purchase_state", PurchaseStateEnum.TIMEOUT);

            if(purchaseMapper.update(null, wrapper1) != 1) {
                //服务器错误，更新失败
                return -1;
            }

            //订单超时，无法支付
            return -2;
        }

        //设置订单为支付完成
        purchase.setPurchaseState(PurchaseStateEnum.FINISHED);
        purchase.setPurchaseFinishedAt(now);
        if(purchaseMapper.update(purchase,wrapper1) != 1){
            //服务器错误，更新失败
            return -1;
        }

        //扣除用户金额
        user.setUserPoint(user.getUserPoint().subtract(purchase.getPurchaseValue()));

        UpdateWrapper<User> wrapper2 = new UpdateWrapper<User>()
                .eq("user_id",user.getUserId())
                .set("user_point",user.getUserPoint());

        if(userMapper.update(null, wrapper2) != 1) {
            //更新用户余额失败
            return -1;
        }

        //支付完成
        return 0;
    }

    //检查用户是否已经购买某文章
    public boolean checkUserHasBoughtArticle(int uid, int aid) {
        Purchase result = selectPurchaseByUidAndPurchaseInfo(uid, PurchaseTypeEnum.ARTICLE, aid);

        if(result == null) {
            return false;
        }

        return result.getPurchaseState() == PurchaseStateEnum.FINISHED;
    }

    public int userCancelPurchase(int uid, Purchase purchase) {
        if(purchase.getPurchaseState() != PurchaseStateEnum.CREATED) {
            //此状态无法取消
            return -3;
        }

        Date now = new Date();
        if(now.getTime() - purchase.getPurchaseCreatedAt().getTime() > 10 * 60 * 1000) {
            UpdateWrapper<Purchase> wrapper1 = new UpdateWrapper<Purchase>().eq("purchase_id", purchase.getPurchaseId());
            //订单已超时，无法取消
            wrapper1.set("purchase_state", PurchaseStateEnum.TIMEOUT);

            if(purchaseMapper.update(null, wrapper1) != 1) {
                //服务器错误，更新失败
                return -1;
            }

            //订单超时，无法取消
            return -2;
        }

        purchase.setPurchaseState(PurchaseStateEnum.CANCELLED);
        UpdateWrapper<Purchase> wrapper = new UpdateWrapper<Purchase>().eq("purchase_id", purchase.getPurchaseId());

        if(purchaseMapper.update(purchase, wrapper) != 1){
            return -1;
        }

        //取消成功
        return 0;
    }
}
